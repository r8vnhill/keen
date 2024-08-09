/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package utils

import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.transform.OutputKeys
import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult
import javax.xml.xpath.XPathConstants
import javax.xml.xpath.XPathFactory
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.logging.Logger
import org.gradle.api.provider.Provider
import org.gradle.api.publish.maven.tasks.GenerateMavenPom
import org.gradle.api.tasks.PathSensitivity.NAME_ONLY
import org.gradle.api.tasks.TaskContainer
import org.gradle.kotlin.dsl.named
import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.Node
import org.w3c.dom.NodeList
import java.io.File

/**
 * Configures the publishing of platform artifacts in the root module of the project.
 *
 * This function configures the tasks for generating Maven POM files for both JVM and Kotlin Multiplatform publications.
 * It ensures that the Kotlin Multiplatform POM file is updated to include dependencies from the JVM POM file and is
 * formatted correctly.
 *
 * @param tasks The task container for the project.
 */
internal fun publishPlatformArtifactsInRootModule(tasks: TaskContainer, logger: Logger) {
   // Retrieve the task for generating the POM file for the JVM publication.
   val jvmPomTask = tasks.named<GenerateMavenPom>("generatePomFileForJvmPublication")

   // Configure the task for generating the POM file for the Kotlin Multiplatform publication.
   tasks.named<GenerateMavenPom>("generatePomFileForKotlinMultiplatformPublication").configure {
      // Map the destination file of the JVM POM task.
      val jvmPom = jvmPomTask.map { it.destination }

      // Setup inputs and normalization for the task.
      setupInputsAndNormalization(this, jvmPom)

      // Define the action to be performed after the task execution.
      doLast("re-write KMP common POM") {
         // Read the original content of the KMP POM file.
         val original = destination.readText()

         // Extract the groupId, artifactId, and version from the JVM POM file.
         val jvmDetails = extractJvmPomDetails(jvmPom.get())

         // Update the KMP POM file with the extracted JVM details.
         val kmpPomDoc = updateKmpPom(destination, jvmDetails)

         // Write the updated KMP POM document to the destination file.
         writeUpdatedPom(kmpPomDoc, destination)

         // Log the information about the original and updated KMP POM content.
         logInformation(logger, path, original, destination)
      }
   }
}

/**
 * Sets up the inputs and normalization for a given task.
 *
 * This function configures the given task to include the JVM POM file as an input, normalizes its line endings, and
 * sets the path sensitivity to NAME_ONLY.
 *
 * @param task The task to configure.
 * @param jvmPom A provider for the JVM POM file.
 */
private fun setupInputsAndNormalization(task: Task, jvmPom: Provider<File>) {
   // Configure the task's inputs to include the JVM POM file.
   task.inputs.file(jvmPom)
      // Set a property name for the input.
      .withPropertyName("jvmPom")
      // Normalize line endings for the input file.
      .normalizeLineEndings()
      // Set the path sensitivity to NAME_ONLY, which means only the file name matters, not the path.
      .withPathSensitivity(NAME_ONLY)
}

/**
 * Extracts the groupId, artifactId, and version from a JVM POM file.
 *
 * This function parses the given POM file and extracts the values for the groupId, artifactId, and version elements.
 * These values are then returned as a [Triple].
 *
 * @param jvmPomFile The JVM POM file from which to extract details.
 * @return A [Triple] containing the groupId, artifactId, and version.
 */
private fun extractJvmPomDetails(jvmPomFile: File): Triple<String, String, String> {
   // Create a new DocumentBuilderFactory and DocumentBuilder for parsing the XML.
   val docFactory = DocumentBuilderFactory.newInstance()
   val docBuilder = docFactory.newDocumentBuilder()

   // Parse the given POM file to create a Document object.
   val jvmDoc = docBuilder.parse(jvmPomFile)

   // Extract the groupId, artifactId, and version elements from the Document.
   val jvmGroupId = jvmDoc.getElement("groupId").textContent
   val jvmArtifactId = jvmDoc.getElement("artifactId").textContent
   val jvmVersion = jvmDoc.getElement("version").textContent

   // Return the extracted values as a Triple.
   return Triple(jvmGroupId, jvmArtifactId, jvmVersion)
}

/**
 * Updates the Kotlin Multiplatform (KMP) POM file with the details from the JVM POM file.
 *
 * This function parses the given KMP POM file, removes any existing platform dependencies, and adds a single dependency
 * on the platform module using the details provided from the JVM POM file. It also sets the packaging type to "pom" to
 * indicate that there's no artifact.
 *
 * @param destination The KMP POM file to be updated.
 * @param jvmDetails A [Triple] containing the groupId, artifactId, and version from the JVM POM file.
 * @return The updated [Document] representing the KMP POM file.
 */
private fun updateKmpPom(destination: File, jvmDetails: Triple<String, String, String>): Document {
   // Create a new DocumentBuilderFactory and DocumentBuilder for parsing the XML.
   val docFactory = DocumentBuilderFactory.newInstance()
   val docBuilder = docFactory.newDocumentBuilder()

   // Parse the given KMP POM file to create a Document object.
   val kmpPomDoc = docBuilder.parse(destination).apply {
      // Remove whitespace nodes to prevent blank lines in the pretty-printed output.
      removeWhitespaceNodes()
      // Set standalone=true to prevent `standalone="no"` in the output.
      xmlStandalone = true
   }

   // Extract the groupId, artifactId, and version from the JVM details.
   val (jvmGroupId, jvmArtifactId, jvmVersion) = jvmDetails

   // Get the dependencies element from the KMP POM Document.
   val dependencies = kmpPomDoc.getElement("dependencies")

   // Remove the original platform dependencies.
   while (dependencies.hasChildNodes()) {
      dependencies.removeChild(dependencies.firstChild)
   }

   // Add a single dependency on the platform module.
   dependencies.appendChild(
      kmpPomDoc.createElement("dependency") {
         appendChild(kmpPomDoc.createElement("groupId", jvmGroupId))
         appendChild(kmpPomDoc.createElement("artifactId", jvmArtifactId))
         appendChild(kmpPomDoc.createElement("version", jvmVersion))
         appendChild(kmpPomDoc.createElement("scope", "compile"))
      }
   )

   // Set packaging to POM to indicate that there's no artifact.
   kmpPomDoc.documentElement.appendChild(
      kmpPomDoc.createElement("packaging", "pom")
   )

   return kmpPomDoc
}

/**
 * Writes the updated Kotlin Multiplatform (KMP) POM document to the destination file.
 *
 * This function uses a transformer to write the provided KMP POM document to the specified destination file. It
 * configures the transformer to format the XML with pretty printing, including setting indentation and retaining the
 * XML declaration.
 *
 * @param kmpPomDoc The updated [Document] representing the KMP POM file.
 * @param destination The file to which the updated POM document will be written.
 */
private fun writeUpdatedPom(kmpPomDoc: Document, destination: File) {
   // Create a new transformer instance for writing the XML document.
   val transformer = TransformerFactory.newInstance().newTransformer().apply {
      // Set pretty printing options.
      setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no") // Include XML declaration in the output.
      setOutputProperty(OutputKeys.INDENT, "yes") // Enable indentation in the output.
      setOutputProperty("{https://xml.apache.org/xslt}indent-amount", "2") // Set the indentation amount to 2 spaces.
   }

   // Transform the document to the destination file.
   transformer.transform(DOMSource(kmpPomDoc), StreamResult(destination))
}

/**
 * Logs information about the re-written Kotlin Multiplatform (KMP) POM file.
 *
 * This function checks if info-level logging is enabled for the project. If it is, it reads the updated content of the
 * KMP POM file from the destination file and logs a message that includes the original and updated content of the KMP
 * POM file.
 *
 * @param logger The logger for the project.
 * @param path The path of the KMP POM file.
 * @param original The original content of the KMP POM file before modification.
 * @param destination The file containing the updated content of the KMP POM file.
 */
private fun logInformation(logger: Logger, path: String, original: String, destination: File) {
   // Check if info-level logging is enabled.
   if (logger.isInfoEnabled) {
      // Read the updated content from the destination file.
      val updated = destination.readText()
      // Log the original and updated content of the KMP POM file.
      logger.info(
         """
            [$path] Re-wrote KMP POM
            ${"=".repeat(25)} original ${"=".repeat(25)}
            $original
            ${"=".repeat(25)} updated  ${"=".repeat(25)}
            $updated
            ${"=".repeat(25)}==========${"=".repeat(25)}
            """.trimIndent()
      )
   }
}

/**
 * Retrieves the first element with the specified tag name from the XML Document.
 *
 * This function searches the XML Document for elements with the specified tag name. If an element is found, it returns
 * the first occurrence. If no such element is found, it throws an IllegalStateException with an error message.
 *
 * @param tagName The name of the tag to retrieve.
 * @return The first [Node] with the specified tag name.
 * @throws IllegalStateException if no element with the specified tag name is found.
 */
private fun Document.getElement(tagName: String): Node =
   getElementsByTagName(tagName).item(0) ?: error("No element named '$tagName' in Document $this")

/**
 * Creates a new XML element with the specified name and content.
 *
 * This function creates a new XML element with the specified name, sets its text content to the provided value, and
 * returns the created element.
 *
 * @param name The name of the element to create.
 * @param content The text content to set for the created element.
 * @return The newly created [Element] with the specified name and content.
 */
private fun Document.createElement(name: String, content: String): Element {
   // Create a new element with the specified name.
   val element = createElement(name)
   // Set the text content of the created element.
   element.textContent = content
   // Return the created element.
   return element
}
/**
 * Creates a new XML element with the specified name and applies the provided configuration.
 *
 * This function creates a new XML element with the specified name and applies the provided configuration to it using
 * the `apply` scope function. The configuration lambda allows customization of the element after it has been created.
 *
 * @param name The name of the element to create.
 * @param configure An optional lambda function to configure the created element. Default is an empty lambda.
 * @return The newly created and configured [Element].
 */
private fun Document.createElement(name: String, configure: Element.() -> Unit = {}): Element =
   createElement(name).apply(configure)

/**
 * Removes all whitespace-only text nodes from the XML document.
 *
 * This function uses an XPath expression to find all text nodes in the XML document that consist only of whitespace and
 * removes them. This helps in cleaning up the document by removing unnecessary whitespace nodes.
 *
 * Source: https://stackoverflow.com/a/979606/4161471
 */
private fun Node.removeWhitespaceNodes() {
   // Create a new XPathFactory instance.
   val xpathFactory = XPathFactory.newInstance()

   // XPath expression to find empty text nodes.
   val xpathExp = xpathFactory.newXPath().compile("//text()[normalize-space(.) = '']")
   // Evaluate the XPath expression to find all empty text nodes in the document.
   val emptyTextNodes = xpathExp.evaluate(this, XPathConstants.NODESET) as NodeList

   // Remove each empty text node from the document.
   for (i in 0 until emptyTextNodes.length) {
      val emptyTextNode = emptyTextNodes.item(i)
      emptyTextNode.parentNode.removeChild(emptyTextNode)
   }
}

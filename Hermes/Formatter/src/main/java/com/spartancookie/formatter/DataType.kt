package com.spartancookie.formatter

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import org.w3c.dom.Node
import org.w3c.dom.NodeList
import org.xml.sax.InputSource
import java.io.ByteArrayInputStream
import java.io.StringWriter
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.transform.OutputKeys
import javax.xml.transform.Transformer
import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult
import javax.xml.xpath.XPath
import javax.xml.xpath.XPathConstants
import javax.xml.xpath.XPathFactory

private const val XML_INDENT_PROPERTY_NAME = "{http://xml.apache.org/xslt}indent-amount"
private const val XML_DEFAULT_INDENT = 4
private const val XML_EXPRESSION = "//text()[normalize-space()='']"

/**
 * Enum class with DataTypes. All DataTypes implement [IFormattable].
 */
enum class DataType : IFormattable {

    XML {
        override fun format(unformattedText: String): String {

            // Turn xml string into a document
            val document = DocumentBuilderFactory.newInstance()
                .newDocumentBuilder()
                .parse(InputSource(ByteArrayInputStream(unformattedText.toByteArray(charset(Charsets.UTF_8.name())))))

            // Remove whitespaces outside tags
            document.normalize()
            val xPath: XPath = XPathFactory.newInstance().newXPath()
            val nodeList: NodeList =
                xPath.evaluate(XML_EXPRESSION, document, XPathConstants.NODESET) as NodeList

            for (i in 0 until nodeList.length) {
                val node: Node = nodeList.item(i)
                node.parentNode.removeChild(node)
            }

            // Setup pretty print options
            val transformer: Transformer = TransformerFactory.newInstance().newTransformer().apply {
                setOutputProperty(XML_INDENT_PROPERTY_NAME, XML_DEFAULT_INDENT.toString())
                setOutputProperty(OutputKeys.ENCODING, "UTF-8")
                setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes")
                setOutputProperty(OutputKeys.INDENT, "yes")
            }

            // Return pretty print xml string
            val stringWriter = StringWriter()
            transformer.transform(DOMSource(document), StreamResult(stringWriter))
            return stringWriter.toString()
        }
    },

    JSON {
        override fun format(unformattedText: String): String {
            isValidJson(unformattedText)

            val objectMapper =
                ObjectMapper().apply { configure(SerializationFeature.INDENT_OUTPUT, true) }
            val tree = objectMapper.readTree(unformattedText)
            return objectMapper.writeValueAsString(tree)
        }
    }
}


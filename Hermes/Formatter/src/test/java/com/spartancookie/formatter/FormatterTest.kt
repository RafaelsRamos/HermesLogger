package com.spartancookie.formatter

import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4


private const val validXMLUnformatted = "<?xml version=\"1.0\"?><catalog><book id=\"bk101\"><author>Gambardella, Matthew</author><title>XML Developer's Guide</title></book><book id=\"bk111\"><author>O'Brien, Tim</author><title>MSXML3: A Comprehensive Guide</title></book><book id=\"bk112\"><author>Galos, Mike</author><title>Visual Studio 7: A Comprehensive Guide</title></book></catalog>"
private const val validXMLFormatted = "<catalog>\r\n" + "    <book id=\"bk101\">\r\n" + "        <author>Gambardella, Matthew</author>\r\n" + "        <title>XML Developer's Guide</title>\r\n" + "    </book>\r\n" + "    <book id=\"bk111\">\r\n" + "        <author>O'Brien, Tim</author>\r\n" + "        <title>MSXML3: A Comprehensive Guide</title>\r\n" + "    </book>\r\n" + "    <book id=\"bk112\">\r\n" + "        <author>Galos, Mike</author>\r\n" + "        <title>Visual Studio 7: A Comprehensive Guide</title>\r\n" + "    </book>\r\n" + "</catalog>\r\n"

private const val validXMLUnformatted2 = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?><customer><firstname>Fred</firstname><surname>Bloggs</surname><DOB>10/12/1970</DOB><addresses><address><addressLine1>1 North Road</addressLine1><addressLine2/><town>Colchester</town><county>Essex</county><postcode>CO9 9JJ</postcode></address><address><addressLine1>2 South Road</addressLine1><addressLine2/><town>Colchester</town><county>Essex</county><postcode>CO8 9SR</postcode></address></addresses><homeTelephone>01334 234567</homeTelephone><businessTelephone>01334 234568</businessTelephone><mobileTelephone>0777 876543</mobileTelephone></customer>"
private const val validXMLFormatted2 = "<customer>\r\n    <firstname>Fred</firstname>\r\n    <surname>Bloggs</surname>\r\n    <DOB>10/12/1970</DOB>\r\n    <addresses>\r\n        <address>\r\n            <addressLine1>1 North Road</addressLine1>\r\n            <addressLine2/>\r\n            <town>Colchester</town>\r\n            <county>Essex</county>\r\n            <postcode>CO9 9JJ</postcode>\r\n        </address>\r\n        <address>\r\n            <addressLine1>2 South Road</addressLine1>\r\n            <addressLine2/>\r\n            <town>Colchester</town>\r\n            <county>Essex</county>\r\n            <postcode>CO8 9SR</postcode>\r\n        </address>\r\n    </addresses>\r\n    <homeTelephone>01334 234567</homeTelephone>\r\n    <businessTelephone>01334 234568</businessTelephone>\r\n    <mobileTelephone>0777 876543</mobileTelephone>\r\n</customer>\r\n"

private const val invalidXML = "<?xml version=\"1.0\"?><catalog><book id=\"bk101\"><author>Gambardella,"

private const val validJSONUnformatted = "{\"glossary\": {\"title\": \"example glossary\",\"GlossDiv\": {\"title\": \"S\",\"GlossList\": {\"GlossEntry\": {\"ID\": \"SGML\",\"SortAs\": \"SGML\",\"GlossTerm\": \"Standard Generalized Markup Language\",\"Acronym\": \"SGML\",\"Abbrev\": \"ISO 8879:1986\",\"GlossDef\": {\"para\": \"A meta-markup language, used to create markup languages such as DocBook.\",\"GlossSeeAlso\": [\"GML\", \"XML\"]},\"GlossSee\": \"markup\"}}}}}"
private const val validJSONFormatted = "{\r\n  \"glossary\" : {\r\n    \"title\" : \"example glossary\",\r\n    \"GlossDiv\" : {\r\n      \"title\" : \"S\",\r\n      \"GlossList\" : {\r\n        \"GlossEntry\" : {\r\n          \"ID\" : \"SGML\",\r\n          \"SortAs\" : \"SGML\",\r\n          \"GlossTerm\" : \"Standard Generalized Markup Language\",\r\n          \"Acronym\" : \"SGML\",\r\n          \"Abbrev\" : \"ISO 8879:1986\",\r\n          \"GlossDef\" : {\r\n            \"para\" : \"A meta-markup language, used to create markup languages such as DocBook.\",\r\n            \"GlossSeeAlso\" : [ \"GML\", \"XML\" ]\r\n          },\r\n          \"GlossSee\" : \"markup\"\r\n        }\r\n      }\r\n    }\r\n  }\r\n}"

private const val invalidJSON = "\"SGML\": \"random\",\"SortAs\": \"SGML\",\"GlossTerm\": \"Standard Generalized Markup Language\",\"Acronym\": \"SGMLeate markup languages such as DocBook.\",\"GlossSeeAlso\": [\"GML\", \"XML\"]},\"GlossSee\": \"markup\"}}}}}"

@RunWith(JUnit4::class)
class FormatterTest {

    @Test
    fun formatValidXML2() {
        val result = DataType.XML.format(validXMLUnformatted2)
        assertEquals(validXMLFormatted2, result)
    }

    @Test
    fun formatValidXML() {
        val result = DataType.XML.format(validXMLUnformatted)
        // Assure content is formatted correctly
        assertEquals(validXMLFormatted, result)
    }

    @Test
    fun formatInvalidXML() {
        val result = try {
            DataType.XML.format(invalidXML)
        } catch (e: Exception) {
            invalidXML
        }
        // Assure it returned the inputted content
        assertEquals(invalidXML, result)
    }

    @Test
    fun formatValidJSON() {
        val result = DataType.JSON.format(validJSONUnformatted)
        // Assure content is formatted correctly
        assertEquals(validJSONFormatted, result)
    }

    @Test
    fun formatInvalidJSON() {
        val result = try {
            DataType.JSON.format(invalidJSON)
        } catch (e: Exception) {
            invalidJSON
        }
        // Assure it returned the inputted content
        assertEquals(invalidJSON, result)
    }
}
//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2017.09.04 at 12:01:24 PM IST 
//


package org.dmg.pmml_4_3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for SVM-REPRESENTATION.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="SVM-REPRESENTATION">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="SupportVectors"/>
 *     &lt;enumeration value="Coefficients"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "SVM-REPRESENTATION")
@XmlEnum
public enum SVMREPRESENTATION {

    @XmlEnumValue("SupportVectors")
    SUPPORT_VECTORS("SupportVectors"),
    @XmlEnumValue("Coefficients")
    COEFFICIENTS("Coefficients");
    private final String value;

    SVMREPRESENTATION(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static SVMREPRESENTATION fromValue(String v) {
        for (SVMREPRESENTATION c: SVMREPRESENTATION.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
//
// Este archivo ha sido generado por la arquitectura JavaTM para la implantaci�n de la referencia de enlace (JAXB) XML v2.2.6 
// Visite <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Todas las modificaciones realizadas en este archivo se perder�n si se vuelve a compilar el esquema de origen. 
// Generado el: PM.01.27 a las 01:53:38 PM CST 
//


package com.navis.argo;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para tGuaranteeOverrideType.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * <p>
 * <pre>
 * &lt;simpleType name="tGuaranteeOverrideType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="FIXED_PRICE"/>
 *     &lt;enumeration value="FREE_NOCHARGE"/>
 *     &lt;enumeration value="DISC_PERCENTAGE"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "tGuaranteeOverrideType")
@XmlEnum
public enum TGuaranteeOverrideType {


    /**
     * Fixed Price.
     * 
     */
    FIXED_PRICE,

    /**
     * Free No Charge.
     * 
     */
    FREE_NOCHARGE,

    /**
     * Discount Percentage.
     * 
     */
    DISC_PERCENTAGE;

    public String value() {
        return name();
    }

    public static TGuaranteeOverrideType fromValue(String v) {
        return valueOf(v);
    }

}

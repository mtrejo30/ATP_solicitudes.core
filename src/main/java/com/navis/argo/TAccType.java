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
 * <p>Clase Java para tAccType.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * <p>
 * <pre>
 * &lt;simpleType name="tAccType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}NCName">
 *     &lt;enumeration value="UNKNOWN"/>
 *     &lt;enumeration value="GENSET_NOSE_MOUNT"/>
 *     &lt;enumeration value="GENSET_UNDERSLUNG"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "tAccType")
@XmlEnum
public enum TAccType {

    UNKNOWN,
    GENSET_NOSE_MOUNT,
    GENSET_UNDERSLUNG;

    public String value() {
        return name();
    }

    public static TAccType fromValue(String v) {
        return valueOf(v);
    }

}

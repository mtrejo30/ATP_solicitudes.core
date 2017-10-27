//
// Este archivo ha sido generado por la arquitectura JavaTM para la implantaci�n de la referencia de enlace (JAXB) XML v2.2.6 
// Visite <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Todas las modificaciones realizadas en este archivo se perder�n si se vuelve a compilar el esquema de origen. 
// Generado el: PM.01.27 a las 01:53:38 PM CST 
//


package com.navis.argo;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * complexes
 * 
 * <p>Clase Java para tComplexes complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="tComplexes">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="complex" maxOccurs="unbounded" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="facilities" type="{http://www.navis.com/argo}tFacilities" minOccurs="0" form="unqualified"/>
 *                 &lt;/sequence>
 *                 &lt;attribute name="id" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                 &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                 &lt;attribute name="time-zone-id" type="{http://www.w3.org/2001/XMLSchema}string" />
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tComplexes", propOrder = {
    "complex"
})
public class TComplexes {

    protected List<TComplexes.Complex> complex;

    /**
     * Gets the value of the complex property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the complex property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getComplex().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TComplexes.Complex }
     * 
     * 
     */
    public List<TComplexes.Complex> getComplex() {
        if (complex == null) {
            complex = new ArrayList<TComplexes.Complex>();
        }
        return this.complex;
    }


    /**
     * <p>Clase Java para anonymous complex type.
     * 
     * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="facilities" type="{http://www.navis.com/argo}tFacilities" minOccurs="0" form="unqualified"/>
     *       &lt;/sequence>
     *       &lt;attribute name="id" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
     *       &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}string" />
     *       &lt;attribute name="time-zone-id" type="{http://www.w3.org/2001/XMLSchema}string" />
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "facilities"
    })
    public static class Complex {

        protected TFacilities facilities;
        @XmlAttribute(name = "id", required = true)
        protected String id;
        @XmlAttribute(name = "name")
        protected String name;
        @XmlAttribute(name = "time-zone-id")
        protected String timeZoneId;

        /**
         * Obtiene el valor de la propiedad facilities.
         * 
         * @return
         *     possible object is
         *     {@link TFacilities }
         *     
         */
        public TFacilities getFacilities() {
            return facilities;
        }

        /**
         * Define el valor de la propiedad facilities.
         * 
         * @param value
         *     allowed object is
         *     {@link TFacilities }
         *     
         */
        public void setFacilities(TFacilities value) {
            this.facilities = value;
        }

        /**
         * Obtiene el valor de la propiedad id.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getId() {
            return id;
        }

        /**
         * Define el valor de la propiedad id.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setId(String value) {
            this.id = value;
        }

        /**
         * Obtiene el valor de la propiedad name.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getName() {
            return name;
        }

        /**
         * Define el valor de la propiedad name.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setName(String value) {
            this.name = value;
        }

        /**
         * Obtiene el valor de la propiedad timeZoneId.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getTimeZoneId() {
            return timeZoneId;
        }

        /**
         * Define el valor de la propiedad timeZoneId.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setTimeZoneId(String value) {
            this.timeZoneId = value;
        }

    }

}

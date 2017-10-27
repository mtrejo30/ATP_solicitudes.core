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
 * yards.
 * 
 * <p>Clase Java para tYards complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="tYards">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="yard" maxOccurs="unbounded" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;attribute name="id" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                 &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                 &lt;attribute name="xps-import-state" use="required">
 *                   &lt;simpleType>
 *                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                       &lt;enumeration value="AWAIT_AUTO_IMPORT"/>
 *                       &lt;enumeration value="AWAIT_MANUAL_IMPORT"/>
 *                       &lt;enumeration value="READY_LOCKED"/>
 *                       &lt;enumeration value="READY"/>
 *                     &lt;/restriction>
 *                   &lt;/simpleType>
 *                 &lt;/attribute>
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
@XmlType(name = "tYards", propOrder = {
    "yard"
})
public class TYards {

    protected List<TYards.Yard> yard;

    /**
     * Gets the value of the yard property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the yard property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getYard().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TYards.Yard }
     * 
     * 
     */
    public List<TYards.Yard> getYard() {
        if (yard == null) {
            yard = new ArrayList<TYards.Yard>();
        }
        return this.yard;
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
     *       &lt;attribute name="id" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
     *       &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}string" />
     *       &lt;attribute name="xps-import-state" use="required">
     *         &lt;simpleType>
     *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *             &lt;enumeration value="AWAIT_AUTO_IMPORT"/>
     *             &lt;enumeration value="AWAIT_MANUAL_IMPORT"/>
     *             &lt;enumeration value="READY_LOCKED"/>
     *             &lt;enumeration value="READY"/>
     *           &lt;/restriction>
     *         &lt;/simpleType>
     *       &lt;/attribute>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    public static class Yard {

        @XmlAttribute(name = "id", required = true)
        protected String id;
        @XmlAttribute(name = "name")
        protected String name;
        @XmlAttribute(name = "xps-import-state", required = true)
        protected String xpsImportState;

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
         * Obtiene el valor de la propiedad xpsImportState.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getXpsImportState() {
            return xpsImportState;
        }

        /**
         * Define el valor de la propiedad xpsImportState.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setXpsImportState(String value) {
            this.xpsImportState = value;
        }

    }

}

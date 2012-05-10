/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.camel.model.dataformat;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.namespace.QName;

import org.apache.camel.model.DataFormatDefinition;
import org.apache.camel.spi.DataFormat;
import org.apache.camel.spi.RouteContext;
import org.apache.camel.util.ObjectHelper;

/**
 * Represents the JAXB2 XML {@link DataFormat}
 *
 * @version 
 */
@XmlRootElement(name = "jaxb")
@XmlAccessorType(XmlAccessType.FIELD)
public class JaxbDataFormat extends DataFormatDefinition {
    @XmlAttribute(required = true)
    private String contextPath;
    @XmlAttribute
    private Boolean prettyPrint;
    @XmlAttribute
    private Boolean ignoreJAXBElement;
    @XmlAttribute
    private Boolean filterNonXmlChars;
    @XmlAttribute
    private String encoding;
    @XmlAttribute
    private Boolean fragment;
    // Partial encoding
    @XmlAttribute
    private String partClass;
    @XmlAttribute
    private String partNamespace;
    @XmlAttribute
    private String nameSpacePrefixMapper;
    @XmlTransient
    private Object nameSpacePrefixMapperInstance;

    public JaxbDataFormat() {
        super("jaxb");
    }

    public JaxbDataFormat(boolean prettyPrint) {
        this();
        setPrettyPrint(prettyPrint);
    }

    public String getContextPath() {
        return contextPath;
    }

    public void setContextPath(String contextPath) {
        this.contextPath = contextPath;
    }

    public Boolean getPrettyPrint() {
        return prettyPrint;
    }

    public void setPrettyPrint(Boolean prettyPrint) {
        this.prettyPrint = prettyPrint;
    }

    public Boolean getIgnoreJAXBElement() {
        return ignoreJAXBElement;
    }

    public void setIgnoreJAXBElement(Boolean ignoreJAXBElement) {
        this.ignoreJAXBElement = ignoreJAXBElement;
    }
    
    public void setFragment(Boolean fragment) {
        this.fragment = fragment;
    }
    
    public Boolean getFragment() {
        return fragment;
    }

    public Boolean getFilterNonXmlChars() {
        return filterNonXmlChars;
    }

    public void setFilterNonXmlChars(Boolean filterNonXmlChars) {
        this.filterNonXmlChars = filterNonXmlChars;
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public String getPartClass() {
        return partClass;
    }

    public void setPartClass(String partClass) {
        this.partClass = partClass;
    }

    public String getPartNamespace() {
        return partNamespace;
    }

    public void setPartNamespace(String partNamespace) {
        this.partNamespace = partNamespace;
    }

    public String getNameSpacePrefixMapper() {
        return nameSpacePrefixMapper;
    }

    public void setNameSpacePrefixMapper(String nameSpacePrefixMapper) {
        this.nameSpacePrefixMapper = nameSpacePrefixMapper;
    }
    
    protected DataFormat createDataFormat(RouteContext routeContext) {
        if (nameSpacePrefixMapper != null) {
            try {
                Class<?> clazz = routeContext.getCamelContext().getClassResolver().resolveMandatoryClass(nameSpacePrefixMapper);
                nameSpacePrefixMapperInstance = clazz.newInstance();
            } catch (Exception e) {
                throw ObjectHelper.wrapRuntimeCamelException(e);
            }   
        }
        
        return super.createDataFormat(routeContext);
    }

    @Override
    protected void configureDataFormat(DataFormat dataFormat) {
        Boolean answer = ObjectHelper.toBoolean(getPrettyPrint());
        if (answer != null && !answer) {
            setProperty(dataFormat, "prettyPrint", Boolean.FALSE);
        } else { // the default value is true
            setProperty(dataFormat, "prettyPrint", Boolean.TRUE);
        }
        answer = ObjectHelper.toBoolean(getIgnoreJAXBElement());
        if (answer != null && !answer) {
            setProperty(dataFormat, "ignoreJAXBElement", Boolean.FALSE);
        } else { // the default value is true
            setProperty(dataFormat, "ignoreJAXBElement", Boolean.TRUE);
        }
        answer = ObjectHelper.toBoolean(getFilterNonXmlChars());
        if (answer != null && answer) {
            setProperty(dataFormat, "filterNonXmlChars", Boolean.TRUE);
        } else { // the default value is false
            setProperty(dataFormat, "filterNonXmlChars", Boolean.FALSE);
        }
        answer = ObjectHelper.toBoolean(getFragment());
        if (answer != null && answer) {
            setProperty(dataFormat, "fragment", Boolean.TRUE);
        } else { // the default value is false
            setProperty(dataFormat, "fragment", Boolean.FALSE);
        }
        if (partClass != null) {
            setProperty(dataFormat, "partClass", partClass);
        }
        if (partNamespace != null) {
            setProperty(dataFormat, "partNamespace", QName.valueOf(partNamespace));
        }
        if (encoding != null) {
            setProperty(dataFormat, "encoding", encoding);
        }
        setProperty(dataFormat, "contextPath", contextPath);
        if (nameSpacePrefixMapperInstance != null) {
            setProperty(dataFormat, "nameSpacePrefixMapper", nameSpacePrefixMapperInstance);
        }
    }
}
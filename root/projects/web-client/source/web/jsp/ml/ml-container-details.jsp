<%--
 * Copyright (C) 2005-2007 Alfresco Software Limited.
 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.

 * As a special exception to the terms and conditions of version 2.0 of 
 * the GPL, you may redistribute this Program in connection with Free/Libre 
 * and Open Source Software ("FLOSS") applications as described in Alfresco's 
 * FLOSS exception.  You should have recieved a copy of the text describing 
 * the FLOSS exception, and it is also available here: 
 * http://www.alfresco.com/legal/licensing"
--%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/alfresco.tld" prefix="a" %>
<%@ taglib uri="/WEB-INF/repo.tld" prefix="r" %>

<%@ page buffer="64kb" contentType="text/html;charset=UTF-8" %>
<%@ page isELIgnored="false" %>
<%@ page import="org.alfresco.web.ui.common.PanelGenerator" %>

<r:page titleId="title_file_details">

<f:view>
  
   
   <%-- load a bundle of properties with I18N strings --%>
   <f:loadBundle basename="alfresco.messages.webclient" var="msg"/>
   
   <h:form acceptcharset="UTF-8" id="document-details">
   
   <%-- Main outer table --%>
   <table cellspacing="0" cellpadding="2">
      
      <%-- Title bar --%>
      <tr>
         <td colspan="2">
            <%@ include file="../parts/titlebar.jsp" %>
         </td>
      </tr>
      
      <%-- Main area --%>
      <tr valign="top">
         <%-- Shelf --%>
         <td>
            <%@ include file="../parts/shelf.jsp" %>
         </td>
         
         <%-- Work Area --%>
         <td width="100%">
            <table cellspacing="0" cellpadding="0" width="100%">
               <%-- Breadcrumb --%>
               <%@ include file="../parts/breadcrumb.jsp" %>
               
               <%-- Status and Actions --%>
               <tr>
                  <td style="background-image: url(<%=request.getContextPath()%>/images/parts/statuspanel_4.gif)" width="4"></td>
                  <td bgcolor="#dfe6ed">
                  
                     <%-- Status and Actions inner contents table --%>
                     <%-- Generally this consists of an icon, textual summary and actions for the current object --%>
                     <table cellspacing="4" cellpadding="0" width="100%">
                        <tr>
                           <td width="32">
                              <img src="<%=request.getContextPath()%>/images/icons/details_large.gif" width=32 height=32>
                           </td>
                           <td>
                              <div class="mainTitle">
                                 <h:outputText value="#{msg.manage_multilingual_details_for}" /> '<h:outputText value="#{DocumentDetailsBean.name}" />'<r:lockIcon value="#{DocumentDetailsBean.document.nodeRef}" align="absmiddle" />
                              </div>                              
                              <div class="mainSubText"><h:outputText id="ml-details-description" value="#{msg.manage_multilingual_details_description}" /></div>
                           </td>                                             
                        </tr>
                     </table>
                  </td>
                  <td style="background-image: url(<%=request.getContextPath()%>/images/parts/statuspanel_6.gif)" width="4"></td>
               </tr>
               
               <%-- separator row with gradient shadow --%>
               <tr>
                  <td><img src="<%=request.getContextPath()%>/images/parts/statuspanel_7.gif" width="4" height="9"></td>
                  <td style="background-image: url(<%=request.getContextPath()%>/images/parts/statuspanel_8.gif)"></td>
                  <td><img src="<%=request.getContextPath()%>/images/parts/statuspanel_9.gif" width="4" height="9"></td>
               </tr>
               
               <%-- Error Messages --%>
               <tr valign=top>
                  <td style="background-image: url(<%=request.getContextPath()%>/images/parts/whitepanel_4.gif)" width=4></td>
                  <td>
                     <%-- messages tag to show messages not handled by other specific message tags --%> 
                     <a:errors message="" infoClass="statusWarningText" errorClass="statusErrorText" />
                  </td>
                  <td style="background-image: url(<%=request.getContextPath()%>/images/parts/whitepanel_6.gif)" width=4></td>
               </tr>
               
               <%-- Details --%>
               <tr valign=top>
                  <td style="background-image: url(<%=request.getContextPath()%>/images/parts/whitepanel_4.gif)" width="4"></td>
                  <td>
                     <table cellspacing="0" cellpadding="3" border="0" width="100%">
                        <tr>
                           <td width="100%" valign="top">
                              <%-- wrapper comment used by the panel to add additional component facets --%>
                              <h:panelGroup id="dashboard-panel-facets">
                                 <f:facet name="title">
                                    <r:permissionEvaluator value="#{DocumentDetailsBean.document}" allow="Write" id="evalChange">
                                       <a:actionLink id="actModify" value="#{msg.modify}" action="dialog:applyTemplate" showLink="false" image="/images/icons/preview.gif" style="padding-right:8px" />
                                       <a:actionLink id="actRemove" value="#{msg.remove}" actionListener="#{DocumentDetailsBean.removeTemplate}" showLink="false" image="/images/icons/delete.gif" />
                                    </r:permissionEvaluator>
                                 </f:facet>
                              </h:panelGroup>

                               
                               <%-- 
                                     Multlingual details 
                                  --%> 
                               
                              <%-- properties for Ml container --%>
                               <h:panelGroup id="ml-props-panel-facets">
                                 <f:facet name="title">
                                    <r:permissionEvaluator value="#{DocumentDetailsBean.documentMlContainer}" allow="Write">
                                       <a:actionLink id="titleLinkMl" value="#{msg.modify}" showLink="false" image="/images/icons/edit_properties.gif"
                                                     action="dialog:editMlContainer" />
                                     </r:permissionEvaluator>
                                  </f:facet>
                                </h:panelGroup>
                                 
                        <a:panel label="#{msg.properties}" facetsId="ml-props-panel-facets" id="ml-properties-panel" progressive="true"
                                       border="white" bgcolor="white" titleBorder="lbgrey" expandedTitleBorder="dotted" titleBgcolor="white" 
                                       expanded='#{DocumentDetailsBean.panels["ml-properties-panel"]}' expandedActionListener="#{DocumentDetailsBean.expandPanel}">
                                                                       

                           <h:outputText styleClass="nodeWorkflowInfoTitle"/>
                           <r:propertySheetGrid id="ml-container-props-sheet" value="#{DocumentDetailsBean.documentMlContainer}" 
                                       var="mlContainerProps" columns="1" labelStyleClass="propertiesLabel" 
                                       externalConfig="true" cellpadding="2" cellspacing="2" mode="view"/>                                                       
                              </a:panel>

                              <div style="padding:4px"></div>

                        <%-- list of translations --%>  
                               <a:panel label="#{msg.translations}" id="ml-translation-panel" progressive="true"
                                       border="white" bgcolor="white" titleBorder="lbgrey" expandedTitleBorder="dotted" titleBgcolor="white" 
                              expanded='#{DocumentDetailsBean.panels["ml-translation-panel"]}' expandedActionListener="#{DocumentDetailsBean.expandPanel}">

                                 <a:richList id="TranslationList" viewMode="details" value="#{DocumentDetailsBean.translations}" 
                                            var="r" styleClass="recordSet" headerStyleClass="recordSetHeader" 
                                            rowStyleClass="recordSetRow" altRowStyleClass="recordSetRowAlt" width="100%" 
                                            pageSize="10" initialSortColumn="Name" initialSortDescending="false">
                                 
                                        <%-- Icon details view mode --%>
                                       <a:column id="col20" primary="true" width="20" style="padding:2px;">
                                           <f:facet name="header">
                                             <h:outputText value=""/>
                                           </f:facet>
                                           <h:graphicImage url="/images/filetypes/_default.gif" />
                                       </a:column>
                              
                                       <%-- Name Columns --%>
                                       <a:column id="col21"  width="300" style="text-align:left">
                                           <f:facet name="header">
                                              <a:sortLink label="#{msg.name}" value="Name" mode="case-insensitive" styleClass="header"/>
                                           </f:facet>
                                           <a:actionLink id="view-name" value="#{r.name}" href="#{r.url}" target="new" />
                                       </a:column>
                                      
                                       <%-- Language columns --%>
                                       <a:column id="col22" width="50" style="text-align:left">
                                          <f:facet name="header">
                                             <a:sortLink label="#{msg.language}" value="language" mode="case-insensitive" styleClass="header"/>
                                          </f:facet>
                                          <h:outputText id="view-language" value="#{r.language}" />
                                       </a:column>
                                                                                                          
                                       <%-- view actions --%>
                                       <a:column id="col25" style="text-align: left">
                                          <f:facet name="header">
                                             <h:outputText value="#{msg.actions}"/>
                                          </f:facet>
                                          <a:actionLink id="view-link" value="#{msg.view}" href="#{r.url}" target="new" />
                                       </a:column>
                
                                       <a:dataPager styleClass="pager" />
                                  </a:richList>                                 
                             </a:panel>




                                                                                          
                           
                           <td valign="top">
                              
                              <% PanelGenerator.generatePanelStart(out, request.getContextPath(), "greyround", "#F5F5F5"); %>
                              <table cellpadding="1" cellspacing="1" border="0" width="100%">
                                 <tr>
                                    <td align="center">
                                       <h:commandButton id="close-btn" value="#{msg.close}" action="dialog:close" styleClass="wizardButton" />
                                    </td>
                                 </tr>
                              </table>
                              <% PanelGenerator.generatePanelEnd(out, request.getContextPath(), "greyround"); %>
                              
                              <div style="padding:4px"></div>
                              
                              <%-- Document Actions --%>
                              <a:panel label="#{msg.actions}" id="actions-panel" border="white" bgcolor="white" titleBorder="lbgrey" expandedTitleBorder="dotted" titleBgcolor="white" style="text-align:center"
                                    progressive="true" expanded='#{DocumentDetailsBean.panels["actions-panel"]}' expandedActionListener="#{DocumentDetailsBean.expandPanel}">
                                 <r:actions id="actions_doc" value="multilingual_details_actions" context="#{DocumentDetailsBean.document}" verticalSpacing="3" style="white-space:nowrap" />
                              </a:panel>
                           </td>
                        </tr>
                     </table>
                  </td>
                  <td style="background-image: url(<%=request.getContextPath()%>/images/parts/whitepanel_6.gif)" width="4"></td>
               </tr>
               
               <%-- separator row with bottom panel graphics --%>
               <tr>
                  <td><img src="<%=request.getContextPath()%>/images/parts/whitepanel_7.gif" width="4" height="4"></td>
                  <td width="100%" align="center" style="background-image: url(<%=request.getContextPath()%>/images/parts/whitepanel_8.gif)"></td>
                  <td><img src="<%=request.getContextPath()%>/images/parts/whitepanel_9.gif" width="4" height="4"></td>
               </tr>
               
            </table>
          </td>
       </tr>
    </table>
    
    </h:form>
    
</f:view>

</r:page>
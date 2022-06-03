<%@page language="java"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="urn:jsptagdir:/WEB-INF/tags"%>

<acme:list>
	<acme:list-column code="inventor.nompa.list.label.code" path="code" width="10%"/>
	<acme:list-column code="inventor.nompa.list.label.theme" path="theme" width="10%"/>
	<acme:list-column code="inventor.nompa.list.label.quantity" path="newQuantity" width="10%"/> 
	<acme:list-column code="inventor.nompa.list.label.startDate" path="startDate" width="10%"/> 
	<acme:list-column code="inventor.nompa.list.label.endDate" path="endDate" width="10%"/> 
</acme:list>
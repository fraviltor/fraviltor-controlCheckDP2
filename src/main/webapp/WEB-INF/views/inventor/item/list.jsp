<%@page language="java"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="urn:jsptagdir:/WEB-INF/tags"%>

<acme:list>
	<acme:list-column code="inventor.item.list.label.name" path="name" width="30%"/>
	<acme:list-column code="inventor.item.list.label.code" path="code" width="20%"/>
	<acme:list-column code="inventor.item.list.label.retail-price" path="newRetailPrice" width="20%"/>
	<acme:list-column code="inventor.item.list.label.published" path="published" width="20%"/>
</acme:list>

<acme:button code="inventor.item.list.button.create" action="/inventor/item/create"/>


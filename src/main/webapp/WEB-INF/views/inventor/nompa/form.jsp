<%@page language="java"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="urn:jsptagdir:/WEB-INF/tags"%>

<acme:form> 
<jstl:choose>
	<jstl:when test="${command == 'create'}">
		<acme:input-textbox code="inventor.nompa.form.label.code" path="code" placeholder="Aa11Bb"/> 
    </jstl:when>
    <jstl:when test="${acme:anyOf(command, 'show, update')}">
		<acme:input-textbox code="inventor.nompa.form.label.code" path="code" placeholder="Aa11Bb-mmddyy"/> 
    </jstl:when>
</jstl:choose>
<acme:input-textbox code="inventor.nompa.form.label.theme" path="theme"/> 
<acme:input-textbox code="inventor.nompa.form.label.statement" path="statement"/>
<acme:input-money code="inventor.nompa.form.label.quantity" path="quantity"/> 
<jstl:choose>
	<jstl:when test="${command == 'show' && newBudget.getCurrency()!=budget.getCurrency()}">
        <acme:input-money code="inventor.nompa.form.label.quantity-conversion" path="newQuantity" readonly="true"/>
    </jstl:when>
</jstl:choose>
<acme:input-moment code="inventor.nompa.form.label.creationMoment" path="creationMoment" readonly="true"/> 
<acme:input-moment code="inventor.nompa.form.label.startDate" path="startDate"/> 
<acme:input-moment code="inventor.nompa.form.label.endDate" path="endDate"/> 
<acme:input-textbox code="inventor.nompa.form.label.moreInfo" path="additionalInfo"/> 

<jstl:choose>
	<jstl:when test="${command== 'create'}">
		<acme:submit code="inventor.nompa.form.button.create" action="/inventor/nompa/create?itemId=${itemId}"/>
	</jstl:when>
	<jstl:when test="${command=='show' }">
		<acme:submit code="inventor.nompa.form.button.update" action="/inventor/nompa/update"/>
		<acme:submit code="inventor.nompa.form.button.delete" action="/inventor/nompa/delete"/>
		<acme:button code="inventor.nompa.form.button.item" action="/inventor/item/show?id=${itemId}"/>
	</jstl:when>
	<jstl:when test="${command=='update' }">
		<acme:submit code="inventor.nompa.form.button.update" action="/inventor/nompa/update"/>
	</jstl:when>
</jstl:choose>
</acme:form>
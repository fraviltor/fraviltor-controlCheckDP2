<%@page language="java"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="urn:jsptagdir:/WEB-INF/tags"%>

<acme:form>
	<acme:input-textbox code="patron.patronage-report.form.label.sequence-number" path="sequenceNumber"/>
	<acme:input-moment code="patron.patronage-report.form.label.creation-moment" path="creationMoment"/>
	<acme:input-textarea code="patron.patronage-report.form.label.memorandum" path="memorandum"/>
	<acme:input-url code="patron.patronage-report.form.label.more-info" path="moreInfo"/>
	<acme:input-textbox code="patron.patronage-report.form.label.patronage-code" path="patronage.code"/>				
</acme:form>
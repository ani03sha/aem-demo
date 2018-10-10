<%@include file="/libs/foundation/global.jsp"%>
<%
org.redquark.demo.core.services.ReadJsonService service = sling.getService(org.redquark.demo.core.services.ReadJsonService.class);

String result = service.getData();

%>

<h2>This page invokes the AEM ReadJsonService</h2>
<h3>RESPONSE: <%=result%></h3>
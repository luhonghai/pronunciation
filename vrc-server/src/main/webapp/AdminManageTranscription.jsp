<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="x" tagdir="/WEB-INF/tags/sub"%>
<t:main pageTitle="Wholesale delivery system" index="0">
  <%--<x:admin pageTitle="admin"></x:admin>--%>
  <x:adminManageTranscription pageTitle="admin"></x:adminManageTranscription>
</t:main>
<script src="<%=request.getContextPath() %>/js/adminTranscription.js"></script>
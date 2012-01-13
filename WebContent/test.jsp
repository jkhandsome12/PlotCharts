<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
<script language="JavaScript" src="Charts/FusionCharts.js"></script>
<script language="JavaScript" src="Charts/jquery.min.js"></script>
</head>
<body>
	网络类型：
	<select id="netType" name="netType">
		<c:forEach items="${netList}" var="netType">
			<option value="${netType}">${netType}</option>
		</c:forEach>
	</select><br />
	监控类型：
	<select id="captionType" name="captionType">
		<option value="1">网络连接时间监控</option>
		<option value="2">网络下载速率监控</option>
		<option value="3">失败与否监控</option> 
	</select>
	<input type="hidden" id="dirPath" name="dirPath" value="${dirPath}" />
	<input type="button" value="button" onclick="generateChart()">
<div id="chartdiv" align="center">Chart will load herefff</div>
<script type="text/javascript">
	function generateChart()
	{
		var netType=$("#netType").val();
		var captionType=$("#captionType").val();
		var dirPath=$("#dirPath").val();
		var chart = new FusionCharts("Charts/ZoomLine.swf", "ChartId", "1200", "700", "0", "0");
		chart.setDataURL( "<%=request.getContextPath()%>/ms?netType="+netType+"&captionType="+captionType+"&dirPath="+dirPath );		   
		chart.render("chartdiv");
	}
</script>
</body>
</html>
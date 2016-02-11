<%@page import="edu.dartmouth.cs.myrunsserver.server.data.PostEntity"%>
<%@page import="java.util.*"%>
<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Post</title>
</head>
<body>
	<b>Post</b>
	<table border="1" style="width:100%">
		<tr>
            <TH>
                ID
            </TH>
            <TH>
                Input Type
            </TH>
            <TH>
                Activity Type
            </TH>
            <TH>
                Date Time
            </TH>
            <TH>
                Duration
            </TH>
            <TH>
                Distance
            </TH>
            <TH>
                Average Speed
            </TH>
            <TH>
                Calories
            </TH>
            <TH>
                Climb
            </TH>
            <TH>
                Heart Rate
            </TH>
            <TH>
                Comment
            </TH>
            <TH>

            </TH>
        </tr>
        
        <% ArrayList<PostEntity> postList = (ArrayList<PostEntity>) request.getAttribute("postList");
			for (PostEntity post : postList) {%>
			<tr>

				<td>
					<%=post.getId()%>
				</td>
				
				<td>
					<%=post.getInputType()%><br>
				</td>
				
				<td>
					<%=post.getActivityType()%><br>
				</td>
				
				<td>
					<%=post.getDateTime()%><br>
				</td>
				
				<td>
					<%=post.getDuration()%><br>
				</td>
				
				<td>
					<%=post.getDistance()%><br>
				</td>
				
				<td>
					<%=post.getAvgSpeed()%><br>
				</td>
				
				<td>
					<%=post.getCalories()%><br>
				</td>
				
				<td>
					<%=post.getClimb()%><br>
				</td>
				
				<td>
					<%=post.getHeartRate()%><br>
				</td>
				
				<td>
					<%=post.getComment()%><br>
				</td>
				
				<td>
					<form name="delete" action="/delete.do" method="post">
						<input type="hidden" name="id_key" id="id_key" value="<%=post.getId()%>"/>
						<input type="submit" value="Delete"/>
					</form>
				</td>
			
			</tr>
				
		<%}%>

	</table>

</body>
</html>

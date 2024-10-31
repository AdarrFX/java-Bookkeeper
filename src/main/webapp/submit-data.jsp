<%@ page import="java.sql.ResultSet" %>
<%@ page import="java.sql.ResultSetMetaData" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Submit Income and Expenses</title>
    <link rel="stylesheet" href="css/styles.css">
    <style>
        body {
            display: flex;
            justify-content: center;
            align-items: center;
            flex-direction: column;
            font-family: Arial, sans-serif;
            background-color: #f9f9f9;
            padding: 20px;
        }

        .container {
            width: 100%;
            max-width: 600px;
            background-color: #ffffff;
            padding: 20px;
            margin-top: 20px;
            box-shadow: 0px 4px 8px rgba(0, 0, 0, 0.1);
            border-radius: 8px;
        }

        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 30px;
        }

        th, td {
            padding: 8px;
            text-align: center;
            border: 1px solid #ddd;
        }

        th {
            background-color: #4CAF50;
            color: white;
        }

        h2 {
            text-align: center;
        }

        #dataForm {
            display: flex;
            flex-direction: column;
            gap: 10px;
        }

        #dataForm label {
            font-weight: bold;
        }

        #dataForm input[type="text"],
        #dataForm input[type="number"],
        #dataForm input[type="date"] {
            padding: 8px;
            width: 100%;
            border: 1px solid #ddd;
            border-radius: 4px;
            background-color: #f8f8f8;
        }

        #dataForm button {
            padding: 10px;
            background-color: #007bff;
            color: white;
            border: none;
            border-radius: 4px;
            cursor: pointer;
        }

        #dataForm button:hover {
            background-color: #0056b3;
        }
    </style>
</head>
<body>
    <div class="header">
        <a href="submit-data.html" style="text-decoration: none; color: white;">
            <span style="font-size: 1.5em;">Java BookKeeper</span>
        </a>
        <nav>
            <ul>
                <li><a href="submit-data.html">Submit Data</a></li>
                <li><a href="view-reports.html">View Reports</a></li>
                <li><a href="index.html">Logout</a></li>
            </ul>
        </nav>
    </div>

    <div class="container">
        <h2>Income and Expenses Tracker</h2>

        <form id="dataForm">
            <label>Date:</label>
            <input type="date" id="date" required>

            <label>Description:</label>
            <input type="text" id="description" required>

            <label>Category:</label>
            <input type="text" id="category" required>

            <label>Income (Money IN):</label>
            <input type="number" id="income" step="0.01">

            <label>Expense (Money OUT):</label>
            <input type="number" id="expense" step="0.01">

            <button type="submit">Add Entry</button>
        </form>

        <table id="dataTable">
            <thead>
                <tr>
                    <th>Date</th>
                    <th>Description</th>
                    <th>Category</th>
                    <th>Income</th>
                    <th>Expense</th>
                    <th>Balance</th>
                </tr>
    		<%
			ResultSet orderResults = (ResultSet) request.getAttribute("orderResults");
			ResultSetMetaData orderResultsMeta = (ResultSetMetaData)request.getAttribute("metaData");

			float totalIncome = 0;
			float totalExpense = 0;
			float totalBalance = 0;

			if (orderResults != null){
			while (orderResults.next()) {
				out.print("<tr>");
				String[] fields = new String[7];

				for (int i = 1; i <= orderResultsMeta.getColumnCount(); i++) {

					fields[i] = orderResults.getString(i);

				}

				out.print("<td>" + fields[5] + "</td>");
				out.print("<td>" + fields[6] + "</td>");
				out.print("<td>" + fields[3] + "</td>");

				if (Float.parseFloat(fields[4]) >= 0){
					out.print("<td>" + fields[4] + "</td>");
					totalIncome += Float.parseFloat(fields[4]);
				} else {
					out.print("<td>" + 0 + "</td>");
				}

				if (Float.parseFloat(fields[4]) < 0) {
					out.print("<td>" + fields[4] + "</td>");
					totalExpense += Float.parseFloat(fields[4]);
				} else {
					out.print("<td>" + 0 + "</td>");
				}

				out.print("<td>" + fields[4] + "</td>");

				out.print("</tr>");
				}
	
			totalBalance = totalIncome + totalExpense;

			}%>
			
            </thead>
            <tbody></tbody>
            <tfoot>
                <tr>
                    <td colspan="3">Total</td>
                    <td id="totalIncome"><% out.print(totalIncome); %></td>
                    <td id="totalExpense"><% out.print(totalExpense); %></td>
                    <td id="overallBalance"><% out.print(totalBalance); %></td>
                </tr>
            </tfoot>
        </table>
    </div>

<script>
    let totalIncome = 0;
    let totalExpense = 0;
    let overallBalance = 0;

    document.getElementById('dataForm').addEventListener('submit', function(event) {
        event.preventDefault();

        const data = {
            date: document.getElementById('date').value,
            description: document.getElementById('description').value,
            category: document.getElementById('category').value,
            income: parseFloat(document.getElementById('income').value) || 0,
            expense: parseFloat(document.getElementById('expense').value) || 0
        };

        console.log("Sending data:", data); 

        fetch('/java-Bookkeeper/processData', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(data)
        })

        .then(response => {
            if (!response.ok) {
            	console.log(response);
                throw new Error('Network response was not ok ' + response.statusText);
            }
            return response.json();
        })
        .then(result => {
            console.log("Response received:", result); 
            if (result.success) {
                alert("Data saved successfully!");


                /* overallBalance += data.income - data.expense;
                totalIncome += data.income;
                totalExpense += data.expense;

                const newRow = document.createElement('tr');
                newRow.innerHTML = `
                    <td>${data.date}</td>
                    <td>${data.description}</td>
                    <td>${data.category}</td>
                    <td>${data.income.toFixed(2)}</td>
                    <td>${data.expense.toFixed(2)}</td>
                    <td>${overallBalance.toFixed(2)}</td>
                `;
                document.querySelector('#dataTable tbody').appendChild(newRow);

                document.getElementById('totalIncome').textContent = totalIncome.toFixed(2);
                document.getElementById('totalExpense').textContent = totalExpense.toFixed(2);
                document.getElementById('overallBalance').textContent = overallBalance.toFixed(2); */


                document.getElementById('dataForm').reset();
                window.location.href = "/processData";
            } else {
                alert("Failed to save data: " + (result.error || 'Unknown error'));
            }
        })
        .catch(error => {
            console.error('Error submitting data:', error);
            alert("Failed to save data due to network error.");
        });
    });
</script>


</body>
</html>
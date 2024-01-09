function downloadReport() {
  // Get the selected from and to dates from the date input fields
  const fromDate = document.getElementById("fromDateInput").value;
  const toDate = document.getElementById("toDateInput").value;

  const fromDateTimestamp = Date.parse(fromDate);
  const toDateTimestamp = Date.parse(toDate);

  // Create an XMLHttpRequest object
  const xhr = new XMLHttpRequest();

  // Construct the absolute URL for the POST request
  const baseUrl = window.location.origin; // Get the base URL

  // Define the URL to send the request to, including the from and to date values
  const url = `${baseUrl}/report/export/parent-report/${fromDateTimestamp}/${toDateTimestamp}`;

  // Open a GET request to the specified URL
  xhr.open("POST", url, true);

  // Set the responseType to blob for downloading a file
  xhr.responseType = "blob";

  // Set up an event listener for when the request is completed
  xhr.onload = function () {
    if (xhr.status === 200) {
      // Create a blob URL for the response data
      const blob = new Blob([xhr.response], {
        type: "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", // Set the correct content type for .xlsx files,
      });
      const url = window.URL.createObjectURL(blob);

      // Create a temporary anchor element to trigger the download
      const a = document.createElement("a");
      a.href = url;
      a.download = "QA Summary Report.xlsx"; // Specify the desired file name with the .xlsx extension
      document.body.appendChild(a);
      a.click();

      // Clean up the temporary anchor element and URL
      window.URL.revokeObjectURL(url);
      document.body.removeChild(a);
    }
  };

  // Send the request
  xhr.send();
}

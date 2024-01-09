function downloadTemplate() {
  const httpRequest = new XMLHttpRequest();
  const baseUrl = window.location.origin;
  const requestURL = `${baseUrl}/report/template`;

  //TODO Please migrate me in a common function
  httpRequest.open("GET", requestURL, true);
  httpRequest.responseType = "blob";

  httpRequest.onload = function () {
    if (httpRequest.status === 200) {
      const blob = new Blob([httpRequest.response], {
        //TODO Please keep me in common constant file
        type: "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
      });
      const url = window.URL.createObjectURL(blob);
      const a = document.createElement("a");
      a.href = url;
      a.download = "QA_Report_Upload_Template.xlsx"; // Corrected the filename
      document.body.appendChild(a);
      a.click();

      window.URL.revokeObjectURL(url);
      document.body.removeChild(a);
    }
  };

  httpRequest.send();
}

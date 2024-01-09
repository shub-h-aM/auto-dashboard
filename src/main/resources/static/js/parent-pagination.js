//Date
function saveDate(dateType) {
console.log("Date is Nan");
  const dateInput = document.getElementById(dateType + "Input");
  const selectedDate = dateInput.value;

  // Save the selected date to Local Storage
  localStorage.setItem(dateType, selectedDate);
}

// Initialize the date fields with stored values on page load
document.addEventListener("DOMContentLoaded", function () {
  const fromDateInput = document.getElementById("fromDateInput");
  const toDateInput = document.getElementById("toDateInput");

  // Retrieve and set the stored values from Local Storage
  const storedFromDate = localStorage.getItem("fromDate");
  const storedToDate = localStorage.getItem("toDate");

  if (storedFromDate) {
    fromDateInput.value = storedFromDate;
  }

  if (storedToDate) {
    toDateInput.value = storedToDate;
  }
});

function getParentReport() {
  // Get the selected dates from the input fields
  var fromDate = document.getElementById("fromDateInput").value;
  var toDate = document.getElementById("toDateInput").value;
  const fromDateTimestamp = Date.parse(fromDate);
  const toDateTimestamp = Date.parse(toDate);

  // Construct the URL with the selected dates and page parameter (assuming page 0 for now)
  var url = `/report/parent-report/1/${fromDateTimestamp}/${toDateTimestamp}`;

  // Redirect to the generated URL
  window.location.href = url;
}

document.addEventListener("DOMContentLoaded", function () {
  // Get the URL from window.location
  var url = window.location.href;

  // Use a regular expression to extract the integer value after "parent-report"
  var match = url.match(/\/parent-report\/(\d+)/);

  if (match) {
    // The integer value is in match[1]
    var extractedValue = match[1];
    console.log(extractedValue); // This will log the extracted integer value
  } else {
    console.log("Integer value after 'parent-report' not found in the URL");
    return; // Exit the function if the value is not found
  }

  const currentPageInput = document.getElementById("currentPage");
  currentPageInput.value = extractedValue;

  //Event listener to the "Next" button
  const nextPageLink = document.getElementById("nextPageLink");
  if (nextPageLink) {
    nextPageLink.addEventListener("click", function (event) {
      event.preventDefault(); // Prevent the default link behavior

      // Get the selected dates from the input fields
      const fromDate = document.getElementById("fromDateInput").value;
      const toDate = document.getElementById("toDateInput").value;

      // Get the current page
      const currentPage = parseInt(
        document.getElementById("currentPage").value
      );

      // Calculate the next page
      const nextPage = currentPage + 1;
      const fromDateTimestamp = Date.parse(fromDate);
      const toDateTimestamp = Date.parse(toDate);

      // Construct the URL with the selected dates and page number
      const url = `/report/parent-report/${nextPage}/${fromDateTimestamp}/${toDateTimestamp}`;

      // Redirect to the generated URL
      window.location.href = url;
    });
  }

  // Add event listener to the "Previous" button
  const previousPageLink = document.querySelector("a.previous-link");
  if (previousPageLink) {
    previousPageLink.addEventListener("click", function (event) {
      event.preventDefault(); // Prevent the default link behavior

      // Get the selected dates from the input fields
      const fromDate = document.getElementById("fromDateInput").value;
      const toDate = document.getElementById("toDateInput").value;

      // Get the current page
      const currentPage = parseInt(
        document.getElementById("currentPage").value
      );

      // Calculate the previous page
      const previousPage = currentPage - 1;

      // Check if previousPage is 0 and set it to 1
      const revisedPreviousPage = previousPage === 0 ? 1 : previousPage;

      const fromDateTimestamp = Date.parse(fromDate);
      const toDateTimestamp = Date.parse(toDate);

      // Construct the URL with the selected dates and page number
      const url = `/report/parent-report/${revisedPreviousPage}/${fromDateTimestamp}/${toDateTimestamp}`;

      // Redirect to the generated URL
      window.location.href = url;
    });
  }

  // Add event listeners to the number buttons ("NumberNextPageLink")
  const numberNextPageLinks = document.querySelectorAll("#NumberNextPageLink");
  numberNextPageLinks.forEach(function (numberNextPageLink) {
    numberNextPageLink.addEventListener("click", function (event) {
      event.preventDefault(); // Prevent the default link behavior

      // Remove the 'active' class from all page number elements
      numberNextPageLinks.forEach(function (link) {
        link.classList.remove("active");
      });

      // Get the selected dates from the input fields
      const fromDate = document.getElementById("fromDateInput").value;
      const toDate = document.getElementById("toDateInput").value;

      // Get the page number from the clicked button
      const pageNumber = parseInt(this.textContent);
      const fromDateTimestamp = Date.parse(fromDate);
      const toDateTimestamp = Date.parse(toDate);

      // Construct the URL with the selected dates and page number
      const url = `/report/parent-report/${pageNumber}/${fromDateTimestamp}/${toDateTimestamp}`;
      // Add the 'active' class to the clicked page number element
      this.classList.add("active");

      // Redirect to the generated URL
      window.location.href = url;
    });
  });
});

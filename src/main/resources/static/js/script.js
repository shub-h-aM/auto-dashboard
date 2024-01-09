console.log("Script Js is running...");

function submitForm() {
  // Show the loader
  const loader = document.getElementById("loader");
  loader.style.display = "block";
  // Get the selected file and date inputs
  const fileInput = document.getElementById("file");
  const fromDateInput = document.getElementById("fromDate");
  const toDateInput = document.getElementById("toDate");

  // Create a FormData object to send the form data
  const formData = new FormData();
  formData.append("file", fileInput.files[0]);

  // Get the values of fromDate and toDate
  const fromDate = fromDateInput.value;
  const toDate = toDateInput.value;

  const fromDateTimestamp = Date.parse(fromDate);
  const toDateTimestamp = Date.parse(toDate);

  // Construct the API endpoint URL with query parameters
  const apiUrl = `/report/upload?fromDate=${fromDateTimestamp}&toDate=${toDateTimestamp}`;

  // Send an AJAX request to the API endpoint
  fetch(apiUrl, {
    method: "POST",
    body: formData,
  })
    .then((response) => response.text())
    .then((data) => {
      // Handle the response from the API
      console.log(data); // You can display a message or perform other actions here
      // Hide the loader
      loader.style.display = "none";

      // Display the success message on the webpage
      const successMessage = document.getElementById("success-message");
      successMessage.textContent = data; // Set the response text as the message
      console.log("Going to show the file....");
      successMessage.classList.remove("hidden"); // Show the message
      successMessage.style.display = "block";
      console.log("Going to showm the file....");
      //Optionally, you can hide the message after a few seconds (e.g., 5 seconds)
      setTimeout(function () {
        successMessage.classList.add("hidden"); // Hide the message
      }, 5000); // 5000 milliseconds (5 seconds)
    })
    .catch((error) => {
      console.error(error);
      // Hide the loader in case of an error
      loader.style.display = "none";
    });
}

// Define a regular expression pattern
var parentPattern = /\/parent-report\/\d+\/?/;
var subPattern = /\/sub-task-report\/\d+$/;
var bugPattern = /\/bug-report\/\d+$/;

// Get the current page URL or determine the current page in some way
var currentPage = window.location.pathname;

// Function to highlight the selected sidebar item
function highlightSidebarItem(linkId) {
  console.log("Inisde hightlite");
  var selectedLink = document.getElementById(linkId);
  if (selectedLink) {
    selectedLink.classList.add("selected");
  }
}

// Check if the pattern matches the current page
var isParentMatching = parentPattern.test(currentPage);
var isSubMatching = subPattern.test(currentPage);
var isBugMatching = bugPattern.test(currentPage);

// Call the highlightSidebarItem function based on the current page
if (isParentMatching) {
  highlightSidebarItem("parent-report-link");
} else if (isSubMatching) {
  highlightSidebarItem("sub-task-report-link");
} else if (isBugMatching) {
  highlightSidebarItem("bug-report-link");
} else if (currentPage.endsWith("/upload-report")) {
  highlightSidebarItem("upload-report-link");
} else if (currentPage.endsWith("/about")) {
  console.log("Inside report");
  highlightSidebarItem("about-report-link");
}

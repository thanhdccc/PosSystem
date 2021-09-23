$(document).ready(function(){
  $("#txtSearchOrder").on("keyup", function() {
    var value = $(this).val().toLowerCase();
    $("#orderTableBody tr").filter(function() {
      $(this).toggle($(this).text().toLowerCase().indexOf(value) > -1)
    });
  });
});
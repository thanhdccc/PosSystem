function validateCreateCustomer() {
	var name = document.getElementById("customer_name").value;
	var email = document.getElementById("customer_email").value;
	var gender = document.getElementById("customer_gender").value;
	var phone = document.getElementById("customer_phone").value;
	var address = document.getElementById("customer_address").value;
	
	if (name === "") {
        alert("Name must not be empty.");
        return;
    }
	
	if (/^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,6}$/.test(email) == false) {
        alert("Email not in right format.");
        return;
    }
    
    if (address === "") {
		address = 'Unknown'
	}
	
    $.ajax({
		type: "POST",
		url: "/api/customer/phone-validate",
		data: { phone : phone},
		dataTye: "text",
		success: function (msg) {
			if (msg == "Error") {
		        alert("Customer with phone already in use.");
		        return;
			} else {
				$.ajax({
					type: "POST",
					url: "/api/customer/order-add-customer",
					data: { name : name, email : email, gender : gender, phone : phone, address : address },
					dataTye: "text",
					success: function (msg) {
						if (msg == "Success") {
							alert("Customer saved successfull.");
                            location.reload();
						} else {
                            alert("Customer save failed.")
						}
					},
					error: function (req, status, error) {
                    	alert(error);
                    }
				});
			}
		},
		error: function (req, status, error) {
        	alert(error);
        }
	});
}
function onSignIn(googleUser) {
    // Useful data for your client-side scripts:
    var profile = googleUser.getBasicProfile();
    console.log("Email: " + profile.getEmail());

    // The ID token you need to pass to your backend:
    //var id_token = googleUser.getAuthResponse().id_token;
    //console.log("ID Token: " + id_token);

    /* function: php ye post request atılacak email varsa kullanıcı bilgileri dönecek ve session phpye post atılacak session phpde session startlanacak ve usertype set edilecek. Usertype'ına göre user anasayfasına yönlendirecek ve diğer tüm sayfalarda user session var mı diye kontrol edilecek yoksa giriş sayfasına redirect edilecek.
     */

    $.post("https://afetkurtar.site/api/users/search.php", JSON.stringify({ email: profile.getEmail() }))
        .done(function(data, status, xhr) {
            console.log("dat: " + data);
            console.log("data retrieved");
            if (xhr.status == 200) {
                console.log("user is found");
                var userData = data.records[0];
                $.post("https://afetkurtar.site/api/config/session.php", userData)
                    .done(function(data, status, xhr) {
                        console.log("data: " + data + "end");
                        console.log("status: " + status);
                        console.log("xhr: " + xhr.status);
                        if (userData.userType == "authorizedUser") {
                            document.location.href = "https://afetkurtar.site/authorizedUser.php";
                        } else if (userData.userType == "personnelUser") {
                            document.location.href = "https://afetkurtar.site/personnelUser.php";
                        } else {
                            document.location.href = "https://afetkurtar.site/volunteerUser.php";
                        }
                    });
            }
        })
        .fail(function(xhr) {
            console.log("xhr:" + xhr.status);
            if (xhr.status == 404) {
                document.location.href = "https://afetkurtar.site/volunteerRegister.php";
            }
        })
        .always(function(data) {
            console.log(data);
            console.log("finished");
        });
}

function registerPersonnel() {

    var email = document.getElementById("personnelEmail").value.trim();
    var username = document.getElementById("personnelUsername").value.trim();
    var role = document.getElementById("personnelRole").value.trim();
    var institution = document.getElementById("personnelInstitution").value.trim();

    if (email == '') {
        alert("Email adresi boş bırakılamaz.");
        return false;
    } else if (username == '') {
        alert("Ad soyad boş bırakılamaz.");
        return false;
    } else if (role == '') {
        alert("Personel rolü boş bırakılamaz.");
        return false;
    } else if (institution == '') {
        alert("Çalıştığı kurum boş bırakılamaz.");
        return false;
    }


    $.post("https://afetkurtar.site/api/users/create.php", JSON.stringify({ userType: 'personnelUser', userName: username, email: email }))
        .done(function(data, status, xhr) {
            //window.alert("data:" + data);

            if (xhr.status == 201) {

                $.post("https://afetkurtar.site/api/personnelUser/create.php",
                        JSON.stringify({ institution: institution, personnelName: username, personnelRole: role, personnelID: data.id }))
                    .done(function(data, status, xhr) {
                        if (xhr.status == 201) {
                            window.alert("Personel kaydı başarıyla gerçekleştirildi.");
                            //document.getElementById('personnelForm').reset();
                        } else {
                            window.alert("Personel kaydı esnasında bir hata ile karşılaşıldı x");
                            //document.getElementById('personnelForm').reset();
                        }
                    });

                document.getElementById('personnelForm').reset();
            } else {
                window.alert("Kullanıcı kaydı esnasında bir hata ile karşılaşıldı y");
                //document.getElementById('personnelForm').reset();
            }
        })
        .fail(function(data, xhr) {
            window.alert("dataf:" + data.message);
            //window.alert("Kullanıcı kaydı esnasında bir hata ile karşılaşıldı z");
            window.alert("xhr:" + xhr.status);
            //document.getElementById('personnelForm').reset();
        });

    
    }
    function sendNotice(){

        var type = document.getElementById("noticeType").value.trim();
        var message = document.getElementById("noticeMessage").value.trim();
        var address = document.getElementById("noticeAddress").value.trim();
        var files = document.getElementById("noticeImage").files;

        if(files.length == 0){
            alert("Afet fotoğrafını yüklemek zorunludur.");
            return false;
        }

        else if (type == '') {
            alert("Afet ihbar türü boş bırakılamaz.");
            return false;
        } else if (address == '') {
            alert("Afet ihbar adresi boş bırakılamaz.");
            return false;
        } else if (message == '') {
            alert("Afet ihbar mesajı boş bırakılamaz.");
            return false;
        }

        var image = files[0];

        var form_data = new FormData();                  
        form_data.append('image', image);
        // alert(form_data);                             
        $.ajax({
            url: 'https://afetkurtar.site/api/uploadImage.php', // point to server-side PHP script 
            dataType: 'text',  // what to expect back from the PHP script, if anything
            cache: false,
            contentType: false,
            processData: false,
            data: form_data,                         
            type: 'post',
            success: function(php_script_response){
                var imageURL = php_script_response;
                var encodedAddress = encodeURI(address);

                $.get("https://maps.googleapis.com/maps/api/geocode/json?address="+encodedAddress+"&sensor=true&key=AIzaSyCxLUKYaDqQEIIQGQGQmC0ipdS04IXRoRw")
                    .done(function(data, status, xhr) {
                        alert(data);
                        var latitude = 0.0;
                        var longitude = 0.0;
                        if(data["status"] == "OK"){
                            latitude = data["results"][0]["geometry"]["location"]["lat"];
                            longitude = data["results"][0]["geometry"]["location"]["lng"];
                        }
                        $.post("https://afetkurtar.site/api/notice/create.php",
                        JSON.stringify({ type: type, message: message, latitude: latitude, longitude: longitude, imageURL: imageURL }))
                        .done(function(data, status, xhr) {
                        if (xhr.status == 201) {
                            window.alert("İhbar başarı ile gönderildi.");
                            //document.getElementById('personnelForm').reset();
                        } else {
                            window.alert("İhbar gönderimi esnasında bir hata ile karşılaşıldı");
                            //document.getElementById('personnelForm').reset();
                        }
                    });
                    });

                

                document.getElementById('noticeForm').reset();
            }
        });
}
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
                // var userData = data.records[0];
                // $.post("https://afetkurtar.site/api/config/session.php", userData)
                //     .done(function(data, status, xhr) {
                //         console.log("data: " + data + "end");
                //         console.log("status: " + status);
                //         console.log("xhr: " + xhr.status);
                //         if (userData.userType == "authorizedUser") {
                //             document.location.href = "https://afetkurtar.site/authorizedUser.php";
                //         } else if (userData.userType == "personnelUser") {
                //             document.location.href = "https://afetkurtar.site/personnelUser.php";
                //         } else {
                //             document.location.href = "https://afetkurtar.site/volunteerUser.php";
                //         }
                //     });
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
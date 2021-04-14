window.map = undefined;



function onSignIn(googleUser) {
    // Useful data for your client-side scripts:
    var profile = googleUser.getBasicProfile();
    console.log("Email: " + profile.getEmail());
    var idToken = profile.id_token;
    googleUser.disconnect();

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

function sendNotice() {

    var type = document.getElementById("noticeType").value.trim();
    var message = document.getElementById("noticeMessage").value.trim();
    var address = document.getElementById("noticeAddress").value.trim();
    var files = document.getElementById("noticeImage").files;

    if (files.length == 0) {
        alert("Afet fotoğrafını yüklemek zorunludur.");
        return false;
    } else if (type == '') {
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
        dataType: 'text', // what to expect back from the PHP script, if anything
        cache: false,
        contentType: false,
        processData: false,
        data: form_data,
        type: 'post',
        success: function(php_script_response) {
            var imageURL = php_script_response;
            var encodedAddress = encodeURI(address);

            $.get("https://maps.googleapis.com/maps/api/geocode/json?address=" + encodedAddress + "&sensor=true&key=AIzaSyCxLUKYaDqQEIIQGQGQmC0ipdS04IXRoRw")
                .done(function(data, status, xhr) {
                    alert(data);
                    var latitude = 0.0;
                    var longitude = 0.0;
                    if (data["status"] == "OK") {
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

function initMap() {
    var temp = document.getElementById("map").id;
    console.log(temp);
    var lat = parseFloat(document.getElementById("map").getAttribute("latitude"));
    var lng = parseFloat(document.getElementById("map").getAttribute("longitude"));
    const map = new google.maps.Map(document.getElementById("map"), {
        zoom: 10,
        center: { lat: lat, lng: lng },
    });
    setMarkers(map);

    map.addListener("click", (mapsMouseEvent) => {
        var position = mapsMouseEvent.latLng.toJSON();

        var subpartAdd = document.getElementById("subpart-add-tab");
        if (subpartAdd != null) {
            $(subpartAdd).tab('show');
            console.log(position);
            console.log(position.lat);
            document.getElementById("subpartLatitude").value = position.lat;
            document.getElementById("subpartLongitude").value = position.lng;
        }

    });

    window.map = map;
}

function setMarkers(map) {
    // Adds markers to the map.
    // Marker sizes are expressed as a Size of X,Y where the origin of the image
    // (0,0) is located in the top left of the image.
    // Origins, anchor positions and coordinates of the marker increase in the X
    // direction to the right and in the Y direction down.
    subparts = document.getElementsByName("subpart");

    const image = {
        url: "https://developers.google.com/maps/documentation/javascript/examples/full/images/beachflag.png",
        // This marker is 20 pixels wide by 32 pixels high.
        size: new google.maps.Size(20, 32),
        // The origin for this image is (0, 0).
        origin: new google.maps.Point(0, 0),
        // The anchor for this image is the base of the flagpole at (0, 32).
        anchor: new google.maps.Point(0, 32),
    };
    // Shapes define the clickable region of the icon. The type defines an HTML
    // <area> element 'poly' which traces out a polygon as a series of X,Y points.
    // The final coordinate closes the poly by connecting to the first coordinate.
    const shape = {
        coords: [1, 1, 1, 20, 18, 20, 18, 1],
        type: "poly",
    };

    for (let i = 0; i < subparts.length; i++) {
        //console.log(subparts[i]);
        var lat = parseFloat(subparts[i].getAttribute("latitude"));
        var lng = parseFloat(subparts[i].getAttribute("longitude"));
        var name = subparts[i].getAttribute("subpart-name");
        var marker = new google.maps.Marker({
            position: { lat: lat, lng: lng },
            map,
            icon: image,
            shape: shape,
            title: name
        });

        google.maps.event.addDomListener(marker, 'click', function() {
            var subpart = document.getElementById("subpart-" + subparts[i].getAttribute("subpart-id") + "-tab");
            $(subpart).tab('show');

        });
    }
}

function initMapForVolunteer() {
    var temp = document.getElementById("map").id;
    console.log(temp);
    var lat = parseFloat(document.getElementById("map").getAttribute("latitude"));
    var lng = parseFloat(document.getElementById("map").getAttribute("longitude"));
    const map = new google.maps.Map(document.getElementById("map"), {
        zoom: 10,
        center: { lat: lat, lng: lng },
    });
    setMarkersForVolunteer(map);
}

function setMarkersForVolunteer(map) {
    // Adds markers to the map.
    // Marker sizes are expressed as a Size of X,Y where the origin of the image
    // (0,0) is located in the top left of the image.
    // Origins, anchor positions and coordinates of the marker increase in the X
    // direction to the right and in the Y direction down.
    subparts = document.getElementsByName("subpart");

    const image = {
        url: "https://developers.google.com/maps/documentation/javascript/examples/full/images/beachflag.png",
        // This marker is 20 pixels wide by 32 pixels high.
        size: new google.maps.Size(20, 32),
        // The origin for this image is (0, 0).
        origin: new google.maps.Point(0, 0),
        // The anchor for this image is the base of the flagpole at (0, 32).
        anchor: new google.maps.Point(0, 32),
    };
    // Shapes define the clickable region of the icon. The type defines an HTML
    // <area> element 'poly' which traces out a polygon as a series of X,Y points.
    // The final coordinate closes the poly by connecting to the first coordinate.
    const shape = {
        coords: [1, 1, 1, 20, 18, 20, 18, 1],
        type: "poly",
    };

    var markers = [];
    var infowindows = [];
    var names = [];
    var lats = [];
    var lngs = [];
    var userIDs = [];
    var currentInfoWindow = null;

    for (let i = 0; i < subparts.length; i++) {
        //console.log(subparts[i]);
        lats[i] = parseFloat(subparts[i].getAttribute("latitude"));
        lngs[i] = parseFloat(subparts[i].getAttribute("longitude"));

        names[i] = subparts[i].getAttribute("subpart-name");

        markers[i] = new google.maps.Marker({
            position: { lat: lats[i], lng: lngs[i] },
            map,
            icon: image,
            shape: shape,
            title: names[i]
        });

        markers[i].index = i;

        userIDs[i] = document.getElementById("user-info").getAttribute("user-id");

        infowindows[i] = new google.maps.InfoWindow({
            content: '<div>' + names[i] + '</div>' + '<input type="submit" onclick="joinSubpart(' + subparts[i].getAttribute("subpart-id") + "," + userIDs[i] + ')" class="btn btn-primary" id="button-' + subparts[i].id + '" value="Gönüllü İsteği Gönder"></input>'
        });

        google.maps.event.addListener(markers[i], 'click', function() {


            if (currentInfoWindow != null) {
                currentInfoWindow.close();
            }
            infowindows[this.index].open(map, markers[this.index]);
            currentInfoWindow = infowindows[this.index];
        });
    }
}

function initMapForPersonnel() {
    var temp = document.getElementById("map").id;
    console.log(temp);
    var lat = parseFloat(document.getElementById("map").getAttribute("latitude"));
    var lng = parseFloat(document.getElementById("map").getAttribute("longitude"));
    const map = new google.maps.Map(document.getElementById("map"), {
        zoom: 10,
        center: { lat: lat, lng: lng },
    });
    //setMarkersForPersonnel(map);
}

function setMarkersForPersonnel(map) { //degisecek
    // Adds markers to the map.
    // Marker sizes are expressed as a Size of X,Y where the origin of the image
    // (0,0) is located in the top left of the image.
    // Origins, anchor positions and coordinates of the marker increase in the X
    // direction to the right and in the Y direction down.
    subparts = document.getElementsByName("subpart");

    const image = {
        url: "https://developers.google.com/maps/documentation/javascript/examples/full/images/beachflag.png",
        // This marker is 20 pixels wide by 32 pixels high.
        size: new google.maps.Size(20, 32),
        // The origin for this image is (0, 0).
        origin: new google.maps.Point(0, 0),
        // The anchor for this image is the base of the flagpole at (0, 32).
        anchor: new google.maps.Point(0, 32),
    };
    // Shapes define the clickable region of the icon. The type defines an HTML
    // <area> element 'poly' which traces out a polygon as a series of X,Y points.
    // The final coordinate closes the poly by connecting to the first coordinate.
    const shape = {
        coords: [1, 1, 1, 20, 18, 20, 18, 1],
        type: "poly",
    };

    var markers = [];
    var infowindows = [];
    var names = [];
    var lats = [];
    var lngs = [];
    var userIDs = [];
    var currentInfoWindow = null;

    for (let i = 0; i < subparts.length; i++) {
        //console.log(subparts[i]);
        lats[i] = parseFloat(subparts[i].getAttribute("latitude"));
        lngs[i] = parseFloat(subparts[i].getAttribute("longitude"));

        names[i] = subparts[i].getAttribute("subpart-name");

        markers[i] = new google.maps.Marker({
            position: { lat: lats[i], lng: lngs[i] },
            map,
            icon: image,
            shape: shape,
            title: names[i]
        });

        markers[i].index = i;

        userIDs[i] = document.getElementById("user-info").getAttribute("user-id");

        infowindows[i] = new google.maps.InfoWindow({
            content: '<div>' + names[i] + '</div>' + '<input type="submit" onclick="joinSubpart(' + subparts[i].getAttribute("subpart-id") + "," + userIDs[i] + ')" class="btn btn-primary" id="button-' + subparts[i].id + '" value="Gönüllü İsteği Gönder"></input>'
        });

        google.maps.event.addListener(markers[i], 'click', function() {


            if (currentInfoWindow != null) {
                currentInfoWindow.close();
            }
            infowindows[this.index].open(map, markers[this.index]);
            currentInfoWindow = infowindows[this.index];
        });
    }
}

function joinSubpart(subpartID, userID) {

    $.post("https://afetkurtar.site/api/volunteerUser/update.php", JSON.stringify({ volunteerID: userID, requestedSubpart: subpartID }))
        .done(function(data, status, xhr) {
            //window.alert("data:" + data);
            console.log(data);
            if (xhr.status == 200) {
                window.alert("Afete gönüllü katılım isteği başarıyla oluşturuldu.");
            }
        })
        .fail(function(data, xhr) {
            //window.alert("dataf:" + data.message);
            window.alert("Afete gönüllü katılım isteğinde hata oluştu");
            //window.alert("xhr:" + xhr.status);
            //document.getElementById('personnelForm').reset();
        });

}

function editDisaster(disasterID) {
    window.location.href = "../editDisaster.php?id=" + disasterID;
}

function joinDisaster(disasterID) {
    window.location.href = "../joinDisaster.php?id=" + disasterID;
}

function addDisaster() {
    window.location.href = "../addDisaster.php";
}

function kisiEkle(subpartID, disasterID) {
    window.location.href = "../kisiEkle.php?id=" + subpartID + "&&disasterID=" + disasterID;
}

$(document).ready(function() {
    var disasterTable = $('#disaster-table');
    if (disasterTable.length) {
        $('#disaster-table').DataTable({
            "pagingType": "full_numbers",
            "language": {
                "decimal": "",
                "emptyTable": "Tabloda veri bulunmamaktadır",
                "info": "_TOTAL_ öğeden _START_ - _END_ arası gösteriliyor",
                "infoEmpty": "0 öğe gösteriliyor",
                "infoFiltered": "(Toplam _MAX_ öğe arasından filtrelenmiştir)",
                "infoPostFix": "",
                "thousands": ",",
                "lengthMenu": "_MENU_ öğe göster",
                "loadingRecords": "Yükleniyor...",
                "processing": "İşleniyor...",
                "search": "Ara:",
                "zeroRecords": "Eşleşen öğe bulunamadı",
                "paginate": {
                    "first": "İlk",
                    "last": "Son",
                    "next": "İleri",
                    "previous": "Geri"
                },
                "aria": {
                    "sortAscending": ": Artan sırada sırala",
                    "sortDescending": ": Azalan sırada sırala"
                }
            }
        });
    }

    var volunteerTable = $('#volunteer-table');
    if (volunteerTable.length) {
        $('#volunteer-table').DataTable({
            "pagingType": "full_numbers",
            "language": {
                "decimal": "",
                "emptyTable": "Tabloda veri bulunmamaktadır",
                "info": "_TOTAL_ öğeden _START_ - _END_ arası gösteriliyor",
                "infoEmpty": "0 öğe gösteriliyor",
                "infoFiltered": "(Toplam _MAX_ öğe arasından filtrelenmiştir)",
                "infoPostFix": "",
                "thousands": ",",
                "lengthMenu": "_MENU_ öğe göster",
                "loadingRecords": "Yükleniyor...",
                "processing": "İşleniyor...",
                "search": "Ara:",
                "zeroRecords": "Eşleşen öğe bulunamadı",
                "paginate": {
                    "first": "İlk",
                    "last": "Son",
                    "next": "İleri",
                    "previous": "Geri"
                },
                "aria": {
                    "sortAscending": ": Artan sırada sırala",
                    "sortDescending": ": Azalan sırada sırala"
                }
            }
        });
    }

    var statusContents = document.getElementsByClassName("status-content");
    var messageContents = document.getElementsByClassName("message-content");

    if (statusContents.length != 0 && messageContents.length != 0) {
        var statusContent = statusContents[0];
        var messageContent = messageContents[0];
        messageContent.scrollIntoView({ behavior: 'smooth', block: 'end' });
        document.getElementById('cd-timeline').scrollIntoView({ behavior: 'smooth', block: 'end' });
        setInterval(function() {
            checkNewStatuses(statusContent);
            checkNewMessages(messageContent)
        }, 1000);
    }

    var selectPicker = document.getElementById("smart-assignment-subparts");
    if (selectPicker) {
        selectPicker.selectpicker();
    }
});

function checkNewMessages(messageContent) {

    var teamID = messageContent.getAttribute("team-id");
    var userID = messageContent.getAttribute("user-id");
    var oldMessageLength = messageContent.getAttribute("message-length");

    $.post("https://afetkurtar.site/api/message/search.php", JSON.stringify({ teamID: teamID }))
        .done(function(data, status, xhr) {

            if (xhr.status == 200) {
                var messages = data["records"];

                if (messages.length != 0 && messages.length != oldMessageLength) {
                    messageContent.setAttribute('message-length', messages.length);
                    messageContent.innerHTML = "";
                    console.log("new messages");


                    for (let i = 0; i < messages.length; i++) {
                        var t = messages[i]['messageTime'].split(/[- :]/);


                        // Apply each element to the Date function
                        var messageTime = t[3] + ':' + t[4];

                        if (messages[i]['userID'] == userID) {
                            messageContent.innerHTML += '<div class="message first">' +
                                messages[i]['messageData'] +
                                '<sub>' + " " + messageTime + '</sub>' +
                                '</div>';
                        } else {
                            messageContent.innerHTML += '<div class="message">' +
                                '<b>' + messages[i]['messageName'] + '</b><br>' +
                                messages[i]['messageData'] +
                                '<sub>' + " " + messageTime + '</sub>' +
                                '</div>';
                        }

                    }
                }
            }
        })
        .fail(function(data, xhr) {
            //window.alert("dataf:" + data.message);
            //window.alert("Yeni mesajlar alınamadı.");
            //window.alert("xhr:" + xhr.status);
            //document.getElementById('personnelForm').reset();
        });

}

function checkNewStatuses(statusContent) {

    var teamID = statusContent.getAttribute("team-id");
    var oldStatusLength = statusContent.getAttribute("status-length");

    $.post("https://afetkurtar.site/api/status/search.php", JSON.stringify({ teamID: teamID }))
        .done(function(data, status, xhr) {

            if (xhr.status == 200) {
                var statuses = data["records"];

                if (statuses.length != 0 && statuses.length != oldStatusLength) {
                    statusContent.setAttribute('status-length', statuses.length);
                    statusContent.innerHTML = "";
                    statusContent.innerHTML = '<section id="cd-timeline" class="cd-container" style="width:48vw;">';
                    console.log("new status");

                    for (let i = 0; i < statuses.length; i++) {
                        statusContent.innerHTML += '<div class="cd-timeline-block">' +
                            '<div class="cd-timeline-img cd-picture">' +
                            '</div>' +
                            '<div class="cd-timeline-content">' +
                            '<h2>' + statuses[i]['statusTime'] + '</h2>' +
                            '<div class="timeline-content-info">' +
                            '<span class="timeline-content-info-date">' +
                            '<i class="fa fa-calendar-o" aria-hidden="true"></i>' +
                            'Takım ' + statuses[i]['teamID'] +
                            '</span>' +
                            '</div>' +
                            '<p>' + statuses[i]['statusMessage'] + '</p>' +
                            '</div> <!-- cd-timeline-content -->' +
                            '</div> <!-- cd-timeline-block -->';
                    }

                    statusContent.innerHTML += '</section> <!-- cd-timeline -->' + '</div>' +
                        '<div class="panel-footer" style="background: #1A2226;box-shadow: 0 3px 6px rgba(0, 0, 0, 0.16), 0 -3px 6px rgba(0, 0, 0, 0.23);">';
                    '<div class="input-group" style="height:7vh; padding:1vh;">' +
                    '<input id="status-input" type="text" class="form-control input-sm chat_input" style="height:5vh;" placeholder="Yeni durum oluşturun..." />' +
                    '<span class="input-group-btn">' +
                    '<button class="btn btn-dark btn-sm" onclick="addStatus(' + teamID + ', ' + statuses[0]['subpartID'] + ')" id="btn-chat" style="height:5vh;">Gönder</button>' +
                        '</span>' +
                        '</div>';
                }
            }
        })
        .fail(function(data, xhr) {
            //window.alert("dataf:" + data.message);
            //window.alert("Yeni durumlar alınamadı.");
            //window.alert("xhr:" + xhr.status);
            //document.getElementById('personnelForm').reset();
        });

}

function updateEmergency(value) {
    $("#emergencyLevelValue").text(value);
}

function updateSubpartEmergency(subpartID, value) {
    $("#emergencyLevelValue" + subpartID).text(value);
}

function sendDisaster() {

    var type = document.getElementById("disasterType").value.trim();
    var name = document.getElementById("disasterName").value.trim();
    var address = document.getElementById("emergencyAddress").value.trim();
    var city = $("#disasterCity option:selected").text();
    var emergencyLevel = document.getElementById("emergencyLevel").value;

    if (type == '') {
        alert("Afet türü boş bırakılamaz.");
        return false;
    } else if (name == '') {
        alert("Afet kayıt ismi boş bırakılamaz.");
        return false;
    } else if (address == '') {
        alert("Afet üssü adresi boş bırakılamaz.");
        return false;
    }

    var encodedAddress = encodeURI(city + " " + address);
    console.log(encodedAddress);

    $.get("https://maps.googleapis.com/maps/api/geocode/json?address=" + encodedAddress + "&sensor=true&key=AIzaSyCxLUKYaDqQEIIQGQGQmC0ipdS04IXRoRw")
        .done(function(data, status, xhr) {
            if (data["status"] == "OK") {
                var latitude = 0.0
                var longitude = 0.0
                latitude = data["results"][0]["geometry"]["location"]["lat"];
                longitude = data["results"][0]["geometry"]["location"]["lng"];

                $.post("https://afetkurtar.site/api/disasterEvents/create.php", JSON.stringify({ disasterType: type, disasterName: name, disasterBase: city, emergencyLevel: emergencyLevel, latitude: latitude, longitude: longitude }))
                    .done(function(data, status, xhr) {
                        //window.alert("data:" + data);
                        console.log(data);
                        if (xhr.status == 201) {
                            window.alert("Afet kaydı başarıyla oluşturuldu.");
                            window.location.href = "../editDisaster.php?id=" + data.id;
                            document.getElementById('disasterForm').reset();
                        } else {
                            window.alert("Afet kaydı esnasında bir hata ile karşılaşıldı y");
                            //document.getElementById('personnelForm').reset();
                        }
                    })
                    .fail(function(data, xhr) {
                        window.alert("dataf:" + data.message);
                        //window.alert("Kullanıcı kaydı esnasında bir hata ile karşılaşıldı z");
                        window.alert("xhr:" + xhr.status);
                        //document.getElementById('personnelForm').reset();
                    });
            } else {
                window.alert("Afet üssü için girdiğiniz adres geçerli değil, lütfen düzeltip yeniden deneyiniz.");
                document.getElementById('disasterForm').reset();
            }
        });
}

$('#v-pills-tab a').on('click', function(e) {
    e.preventDefault();
    $(this).tab('show');
    //map = new google.maps.Map(document.getElementById('map'));



    if (map != null && e.target.id != "subpart-add-tab") {
        var id = e.target.getAttribute("subpart-id");
        var latitude = parseFloat(document.getElementById("subpartLatitude" + id).value);
        var longitude = parseFloat(document.getElementById("subpartLongitude" + id).value);
        console.log(latitude);
        console.log(longitude);
        const center = new google.maps.LatLng(latitude, longitude);
        // using global variable:
        window.map.panTo(center);
    }
    //map.setCenter(new google.maps.LatLng(-34.397, 150.644)); //degisecek
})

$('#v-pills-tab-subpart a').on('click', function(e) {
    e.preventDefault();
    $(this).tab('show');
})


function registerVolunteer() {

    var email = document.getElementById("volunteerEmail").value.trim();
    var username = document.getElementById("volunteerUsername").value.trim();
    var address = document.getElementById("volunteerAddress").value.trim();
    var tc = document.getElementById("volunteerTc").value.trim();
    var tel = document.getElementById("volunteerTel").value.trim();
    //var birthDate = document.getElementById("volunteerBirthDate").value.trim();

    if (email == '') {
        alert("Email adresi boş bırakılamaz.");
        return false;
    } else if (username == '') {
        alert("Ad soyad boş bırakılamaz.");
        return false;
    } else if (address == '') {
        alert("Adres boş bırakılamaz.");
        return false;
    } else if (tc == '') {
        alert("Kimlik numarası boş bırakılamaz.");
        return false;
    } else if (tel == '') {
        alert("Telefon numarası boş bırakılamaz.");
        return false;
    } //else if (birthDate == '') {
    //     alert("Doğum Tarihi boş bırakılamaz.");
    //     return false;
    // }


    $.post("https://afetkurtar.site/api/users/create.php", JSON.stringify({ userType: 'volunteerUser', userName: username, email: email }))
        .done(function(data, status, xhr) {
            if (xhr.status == 201) {
                $.post("https://afetkurtar.site/api/volunteerUser/create.php",
                        JSON.stringify({ volunteerName: username, address: address, tc: tc, tel: tel, volunteerID: data.id }))
                    .done(function(data, status, xhr) {
                        if (xhr.status == 201) {
                            window.alert("Kaydınız başarıyla gerçekleştirildi.");
                            document.location.href = "https://afetkurtar.site";
                            //document.getElementById('personnelForm').reset();
                        } else {
                            window.alert("Kayıt esnasında bir hata ile karşılaşıldı.");
                            //document.getElementById('personnelForm').reset();
                        }
                    });

                document.getElementById('volunteerForm').reset();
            } else {
                window.alert("Kullanıcı kaydı esnasında bir hata ile karşılaşıldı");
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

function updateSubpart(id, status) {
    var subpartName = document.getElementById("subpartName" + id).value.trim();
    var latitude = document.getElementById("subpartLatitude" + id).value.trim();
    var longitude = document.getElementById("subpartLongitude" + id).value.trim();
    var subpartMissingPerson = document.getElementById("subpartMissingPerson" + id).value.trim();
    var subpartRescuedPerson = document.getElementById("subpartRescuedPerson" + id).value.trim();
    var isOpenForVolunteersObject = document.getElementById("subpartIsOpenForVolunteers" + id);
    var emergencyLevel = document.getElementById("emergencyLevel" + id).value;
    var disasterID = parseFloat(document.getElementById("map").getAttribute("disaster-id"));
    var disasterBase = parseFloat(document.getElementById("map").getAttribute("disaster-base"));
    var disasterName = parseFloat(document.getElementById("map").getAttribute("disaster-name"));

    var isOpenForVolunteers = 0;
    if (isOpenForVolunteersObject.checked) {
        isOpenForVolunteers = 1;
    }

    if (subpartName == '') {
        alert("Bölge adı boş bırakılamaz.");
        return false;
    } else if (isNaN(latitude) || latitude == "") {
        alert("Enlem bilgisini kontrol ediniz.");
        return false;
    } else if (isNaN(longitude) || longitude == "") {
        alert("Boylam bilgisini kontrol ediniz.");
        return false;
    } else if (isNaN(subpartMissingPerson) || subpartMissingPerson == "") {
        alert("Kayıp insan sayısı bilgisini kontrol ediniz.");
        return false;
    } else if (isNaN(subpartRescuedPerson) || subpartRescuedPerson == "") {
        alert("Kurtarılan insan sayısı bilgisini kontrol ediniz.");
        return false;
    }


    $.post("https://afetkurtar.site/api/subpart/update.php", JSON.stringify({
            subpartID: id,
            subpartName: subpartName,
            latitude: latitude,
            longitude: longitude,
            missingPerson: subpartMissingPerson,
            isOpenForVolunteers: isOpenForVolunteers,
            disasterID: disasterID,
            address: disasterBase,
            disasterName: disasterName,
            emergencyLevel: emergencyLevel,
            rescuedPerson: subpartRescuedPerson,
            status: status
        }))
        .done(function(data, status, xhr) {
            if (xhr.status == 201) {
                window.alert("Bölge güncellendi.");
                document.location.href = "https://afetkurtar.site/editDisaster.php?id=" + disasterID;
            } else {
                window.alert("Bölge güncellenirken hata oluştu.");
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

function deleteSubpart(subpartID) {
    $.post("https://afetkurtar.site/api/team/search.php", JSON.stringify({ assignedSubpartID: subpartID }))
        .done(function(data, status, xhr) {
            if (xhr.status == 200) {
                if (data.records.length != 0) {
                    var team = data.records[0];
                    var teamID = team.teamID;
                    $.post("https://afetkurtar.site/api/personnelUser/search.php", JSON.stringify({ teamID: teamID }))
                        .done(function(data, status, xhr) {
                            if (xhr.status == 200) {
                                if (data.records.length != 0) {
                                    var personnelUsers = data.records;
                                    personnelUsers.forEach(personnelUser => {
                                        $.post("https://afetkurtar.site/api/personnelUser/update.php", JSON.stringify({ personnelID: personnelUser.personnelID, teamID: "0" }))
                                            .done(function(data, status, xhr) {
                                                if (xhr.status == 201) {
                                                    //window.alert("Personel bölgeden çıkarıldı.");
                                                }
                                            })
                                            .fail(function(data, xhr) {
                                                //window.alert("Bölge silinirken hata oluştu 6.");
                                            });
                                    });

                                }
                            }
                        })
                        .fail(function(data, xhr) {
                            window.alert("Bölge silinirken hata oluştu 7.");
                        });

                    $.post("https://afetkurtar.site/api/volunteerUser/search.php", JSON.stringify({ assignedTeamID: teamID }))
                        .done(function(data, status, xhr) {
                            if (xhr.status == 200) {
                                if (data.records.length != 0) {
                                    var volunteerUsers = data.records;
                                    volunteerUsers.forEach(volunteerUser => {
                                        $.post("https://afetkurtar.site/api/volunteerUser/update.php", JSON.stringify({ volunteerID: volunteerUser.volunteerID, assignedTeamID: "0" }))
                                            .done(function(data, status, xhr) {
                                                if (xhr.status == 201) {
                                                    $.post("https://afetkurtar.site/api/users/update.php", JSON.stringify({ userID: volunteerUser.volunteerID, userType: "volunteerUser" }))
                                                        .done(function(data, status, xhr) {
                                                            if (xhr.status == 201) {
                                                                //window.alert("Gönüllü bölgeden çıkarıldı.");
                                                            }
                                                        })
                                                        .fail(function(data, xhr) {
                                                            //window.alert("Bölge silinirken hata oluştu 3.");
                                                        });
                                                }
                                            })
                                            .fail(function(data, xhr) {
                                                //window.alert("Bölge silinirken hata oluştu 4.");
                                            });
                                    });

                                }
                            }
                        })
                        .fail(function(data, xhr) {
                            //window.alert("Bölge silinirken hata oluştu 5.");
                        });

                    $.post("https://afetkurtar.site/api/subpart/delete.php", JSON.stringify({ subpartID: subpartID }))
                        .done(function(data, status, xhr) {
                            if (xhr.status == 201) {
                                window.alert("Bölge silindi.");
                                //document.location.href = "https://afetkurtar.site/editDisaster.php?id=" + disasterID;
                                document.location.reload();
                            } else {
                                window.alert("Bölge silinirken hata oluştu.");
                                //document.getElementById('personnelForm').reset();
                            }
                        })
                        .fail(function(data, xhr) {
                            window.alert("Bölge silinirken hata oluştu 2.");
                        });
                }

            }
        })
        .fail(function(data, xhr) {
            $.post("https://afetkurtar.site/api/subpart/delete.php", JSON.stringify({
                    subpartID: subpartID
                }))
                .done(function(data, status, xhr) {
                    if (xhr.status == 201) {
                        window.alert("Bölge silindi.");
                        //document.location.href = "https://afetkurtar.site/editDisaster.php?id=" + disasterID;
                        document.location.reload();
                    } else {
                        window.alert("Bölge silinirken hata oluştu. 0");
                        //document.getElementById('personnelForm').reset();
                    }
                })
                .fail(function(data, xhr) {
                    window.alert("Bölge silinirken hata oluştu. 00");
                });

        });


}

function addSubpart() {

    var subpartName = document.getElementById("subpartName").value.trim();
    var latitude = document.getElementById("subpartLatitude").value.trim();
    var longitude = document.getElementById("subpartLongitude").value.trim();
    var subpartMissingPerson = document.getElementById("subpartMissingPerson").value.trim();
    var isOpenForVolunteersObject = document.getElementById("subpartIsOpenForVolunteers");
    var emergencyLevel = document.getElementById("emergencyLevel").value;
    var disasterID = parseFloat(document.getElementById("map").getAttribute("disaster-id"));
    var disasterBase = parseFloat(document.getElementById("map").getAttribute("disaster-base"));
    var disasterName = parseFloat(document.getElementById("map").getAttribute("disaster-name"));

    var isOpenForVolunteers = 0;
    if (isOpenForVolunteersObject.checked) {
        isOpenForVolunteers = 1;
    }

    if (subpartName == '') {
        alert("Bölge adı boş bırakılamaz.");
        return false;
    } else if (isNaN(latitude) || latitude == "") {
        alert("Enlem bilgisini kontrol ediniz.");
        return false;
    } else if (isNaN(longitude) || longitude == "") {
        alert("Boylam bilgisini kontrol ediniz.");
        return false;
    } else if (isNaN(subpartMissingPerson) || subpartMissingPerson == "") {
        alert("Kayıp insan sayısı bilgisini kontrol ediniz.");
        return false;
    }



    $.post("https://afetkurtar.site/api/subpart/create.php", JSON.stringify({
            subpartName: subpartName,
            latitude: latitude,
            longitude: longitude,
            missingPerson: subpartMissingPerson,
            isOpenForVolunteers: isOpenForVolunteers,
            disasterID: disasterID,
            address: disasterBase,
            disasterName: disasterName,
            emergencyLevel: emergencyLevel,
            rescuedPerson: "0",
            status: ""
        }))
        .done(function(data, status, xhr) {
            if (xhr.status == 201) {
                window.alert("Yeni bölge eklendi.");
                document.location.href = "https://afetkurtar.site/editDisaster.php?id=" + disasterID;
            } else {
                window.alert("Bölge eklenirken hata oluştu.");
                //document.getElementById('personnelForm').reset();
            }
        })
        .fail(function(data, xhr) {
            window.alert("Afet bölgesi oluşturulamadı.");
        });
}

function searchVolunteers() {
    var volunteerTable = $('#volunteer-table').DataTable();
    volunteerTable.clear().draw();

    var subpartID = document.getElementById("subpartSelect").value;

    console.log(subpartID);

    $.post("https://afetkurtar.site/api/volunteerUser/search.php", JSON.stringify({ requestedSubpart: subpartID }))
        .done(function(data, status, xhr) {
            if (xhr.status == 200) {

                var volunteers = data.records;

                for (let i = 0; i < volunteers.length; i++) {
                    var experienced = "";
                    var firstAid = "";

                    if (volunteers[i].isExperienced == 1)
                        experienced = "Var";
                    else
                        experienced = "Yok";

                    if (volunteers[i].haveFirstAidCert == 1)
                        firstAid = "Var";
                    else
                        firstAid = "Yok";

                    volunteerTable.row.add([volunteers[i].volunteerName, volunteers[i].tc, volunteers[i].tel, volunteers[i].address, experienced, firstAid, '<input type="button" onclick="addVolunteerToSubpart(' + volunteers[i].volunteerID + "," + subpartID + ')" class="btn btn-primary" id="registerBtn" value="Afet Bölgesine Ata"></input>']).draw(false);

                }
            }
        })
        .fail(function(data, xhr) {
            window.alert("Bu afet bölgesi için gönüllü katılım isteği bulunamadı.");
        });

}

function addVolunteerToSubpart(volunteerID, subpartID) {

    console.log(volunteerID);
    $.post("https://afetkurtar.site/api/team/search.php", JSON.stringify({ assignedSubpartID: subpartID }))
        .done(function(data, status, xhr) {
            if (xhr.status == 200) {
                if (data.records.length != 0) {
                    var team = data.records[0];
                    var teamID = team.teamID;

                    $.post("https://afetkurtar.site/api/volunteerUser/update.php", JSON.stringify({ volunteerID: volunteerID, responseSubpart: subpartID, assignedTeamID: teamID, requestedSubpart: "0" }))
                        .done(function(data, status, xhr) {
                            if (xhr.status == 200) {
                                $.post("https://afetkurtar.site/api/users/update.php", JSON.stringify({ userID: volunteerID, userType: "personnelUser" }))
                                    .done(function(data, status, xhr) {
                                        window.alert("Gönüllü kullanıcı başarıyla göreve atandı.");
                                        window.location.href = "https://afetkurtar.site/gonulluIstekleri.php";
                                    })
                                    .fail(function(data, xhr) {
                                        window.alert("Gönüllü kullanıcı, personel olarak atanamadı.");
                                    });

                            }
                        })
                        .fail(function(data, xhr) {
                            window.alert("Gönüllü kullanıcı güncellenemedi.");
                        });
                }

            }
        })
        .fail(function(data, xhr) {

            $.post("https://afetkurtar.site/api/team/create.php", JSON.stringify({ assignedSubpartID: subpartID }))
                .done(function(data, status, xhr) {
                    var teamID = data.id;

                    $.post("https://afetkurtar.site/api/volunteerUser/update.php", JSON.stringify({ volunteerID: volunteerID, responseSubpart: subpartID, assignedTeamID: teamID, requestedSubpart: 1 }))
                        .done(function(data, status, xhr) {
                            if (xhr.status == 200) {
                                $.post("https://afetkurtar.site/api/users/update.php", JSON.stringify({ userID: volunteerID, userType: "personnelUser" }))
                                    .done(function(data, status, xhr) {
                                        window.alert("Gönüllü kullanıcı başarıyla göreve atandı.");
                                        window.location.href = "https://afetkurtar.site/gonulluIstekleri.php";
                                    })
                                    .fail(function(data, xhr) {
                                        window.alert("Gönüllü kullanıcı, personel olarak atanamadı.");
                                    });

                            }
                        })
                        .fail(function(data, xhr) {
                            window.alert("Gönüllü kullanıcı güncellenemedi.");
                        });
                })
                .fail(function(data, xhr) {
                    window.alert("Gönüllü eklenirken yeni takım oluşturulamadı.");
                });

        });

}

function addPersonnelToSubpart(personnelID, subpartID, disasterID, role) {

    $.post("https://afetkurtar.site/api/team/search.php", JSON.stringify({ assignedSubpartID: subpartID }))
        .done(function(data, status, xhr) {
            if (xhr.status == 200) {
                if (data.records.length != 0) {
                    var team = data.records[0];
                    var teamID = team.teamID;

                    $.post("https://afetkurtar.site/api/personnelUser/update.php", JSON.stringify({ personnelID: personnelID, teamID: teamID, personnelRole: role }))
                        .done(function(data, status, xhr) {
                            if (xhr.status == 201) {
                                window.alert("Personel başarıyla göreve atandı.");
                                window.location.href = "https://afetkurtar.site/editDisaster.php?id=" + disasterID;
                            }
                        })
                        .fail(function(data, xhr) {
                            window.alert("Personel atanırken hata oluştu.");
                        });
                }
            }
        })
        .fail(function(data, xhr) {

            $.post("https://afetkurtar.site/api/team/create.php", JSON.stringify({ assignedSubpartID: subpartID }))
                .done(function(data, status, xhr) {
                    var teamID = data.id;

                    $.post("https://afetkurtar.site/api/personnelUser/update.php", JSON.stringify({ personnelID: personnelID, teamID: teamID, personnelRole: role }))
                        .done(function(data, status, xhr) {
                            if (xhr.status == 201) {
                                window.alert("Personel başarıyla göreve atandı.");
                                window.location.href = "https://afetkurtar.site/editDisaster.php?id=" + disasterID;
                            }
                        })
                        .fail(function(data, xhr) {
                            window.alert("Personel atanırken hata oluştu.");
                        });
                })
                .fail(function(data, xhr) {
                    window.alert("Personel eklenirken yeni takım oluşturulamadı.");
                });

        });

}

function removePersonnelFromTeam(personnelID) {
    $.post("https://afetkurtar.site/api/personnelUser/update.php", JSON.stringify({ personnelID: personnelID, teamID: "0" }))
        .done(function(data, status, xhr) {
            if (xhr.status == 201) {
                window.alert("Personel takımdan çıkarıldı.");
                document.location.reload();
            }
        })
        .fail(function(data, xhr) {
            window.alert("Personel takımdan çıkarılırken hata oluştu.");
        });
}

function removeVolunteerFromTeam(volunteerID) {
    $.post("https://afetkurtar.site/api/volunteerUser/update.php", JSON.stringify({ volunteerID: volunteerID, responseSubpart: "0", assignedTeamID: "0", requestedSubpart: "0" }))
        .done(function(data, status, xhr) {
            if (xhr.status == 200) {
                $.post("https://afetkurtar.site/api/users/update.php", JSON.stringify({ userID: volunteerID, userType: "volunteerUser" }))
                    .done(function(data, status, xhr) {
                        window.alert("Gönüllü kullanıcı takımdan çıkarıldı.");

                    })
                    .fail(function(data, xhr) {
                        window.alert("Gönüllü kullanıcı takımdan çıkarılamadı.");
                    });
            }
        })
        .fail(function(data, xhr) {
            window.alert("Gönüllü kullanıcı takımdan çıkarılamadı.");
        });

}

function addStatus(teamID, subpartID) {

    var statusMessage = document.getElementById("status-input").value.trim();


    if (statusMessage == '') {
        return false;
    }

    $.post("https://afetkurtar.site/api/status/create.php", JSON.stringify({
            statusMessage: statusMessage,
            teamID: teamID,
            subpartID: subpartID
        }))
        .done(function(data, status, xhr) {
            if (xhr.status == 201) {
                //document.location.reload();
            } else {
                window.alert("Durum oluşturulurken hata oluştu.");
            }
        })
        .fail(function(data, xhr) {
            window.alert("Durum oluşturulamadı.");
        });
    var statusContents = document.getElementsByClassName("status-content");
    if (statusContents.length != 0) {
        document.getElementById('cd-timeline').scrollIntoView({ behavior: 'smooth', block: 'end' });
    }

    document.getElementById("status-input").value = "";
}

function addMessage(teamID, userID, userName) {

    var messageData = document.getElementById("message-input").value.trim();


    if (messageData == '') {
        return false;
    }

    $.post("https://afetkurtar.site/api/message/create.php", JSON.stringify({
            messageData: messageData,
            teamID: teamID,
            userID: userID,
            messageName: userName
        }))
        .done(function(data, status, xhr) {
            if (xhr.status == 201) {
                //document.location.reload();
            } else {
                window.alert("Mesaj gönderilirken hata oluştu.");
            }
        })
        .fail(function(data, xhr) {
            window.alert("Mesaj gönderilemedi.");
        });
    var messageContents = document.getElementsByClassName("message-content");
    if (messageContents.length != 0) {
        var messageContent = messageContents[0];
        messageContent.scrollIntoView({ behavior: 'smooth', block: 'end' });
    }

    document.getElementById("message-input").value = "";

}

function deleteSubpartForDisaster(subpartID) {
    $.post("https://afetkurtar.site/api/team/search.php", JSON.stringify({ assignedSubpartID: subpartID }))
        .done(function(data, status, xhr) {
            if (xhr.status == 200) {
                if (data.records.length != 0) {
                    var team = data.records[0];
                    var teamID = team.teamID;
                    $.post("https://afetkurtar.site/api/personnelUser/search.php", JSON.stringify({ teamID: teamID }))
                        .done(function(data, status, xhr) {
                            if (xhr.status == 200) {
                                if (data.records.length != 0) {
                                    var personnelUsers = data.records;
                                    personnelUsers.forEach(personnelUser => {
                                        $.post("https://afetkurtar.site/api/personnelUser/update.php", JSON.stringify({ personnelID: personnelUser.personnelID, teamID: "0" }))
                                            .done(function(data, status, xhr) {
                                                if (xhr.status == 201) {
                                                    //window.alert("Personel bölgeden çıkarıldı.");
                                                }
                                            })
                                            .fail(function(data, xhr) {
                                                //window.alert("Bölge silinirken hata oluştu 6.");
                                            });
                                    });

                                }
                            }
                        })
                        .fail(function(data, xhr) {
                            //window.alert("Bölge silinirken hata oluştu 7.");
                        });

                    $.post("https://afetkurtar.site/api/volunteerUser/search.php", JSON.stringify({ assignedTeamID: teamID }))
                        .done(function(data, status, xhr) {
                            if (xhr.status == 200) {
                                if (data.records.length != 0) {
                                    var volunteerUsers = data.records;
                                    volunteerUsers.forEach(volunteerUser => {
                                        $.post("https://afetkurtar.site/api/volunteerUser/update.php", JSON.stringify({ volunteerID: volunteerUser.volunteerID, assignedTeamID: "0" }))
                                            .done(function(data, status, xhr) {
                                                if (xhr.status == 201) {
                                                    $.post("https://afetkurtar.site/api/users/update.php", JSON.stringify({ userID: volunteerUser.volunteerID, userType: "volunteerUser" }))
                                                        .done(function(data, status, xhr) {
                                                            if (xhr.status == 201) {
                                                                //window.alert("Gönüllü bölgeden çıkarıldı.");
                                                            }
                                                        })
                                                        .fail(function(data, xhr) {
                                                            //window.alert("Bölge silinirken hata oluştu 3.");
                                                        });
                                                }
                                            })
                                            .fail(function(data, xhr) {
                                                //window.alert("Bölge silinirken hata oluştu 4.");
                                            });
                                    });

                                }
                            }
                        })
                        .fail(function(data, xhr) {
                            //window.alert("Bölge silinirken hata oluştu 5.");
                        });

                    $.post("https://afetkurtar.site/api/subpart/delete.php", JSON.stringify({ subpartID: subpartID }))
                        .done(function(data, status, xhr) {
                            if (xhr.status == 201) {
                                //window.alert("Bölge silindi.");
                                //document.location.href = "https://afetkurtar.site/editDisaster.php?id=" + disasterID;
                                //document.location.reload();
                            } else {
                                //window.alert("Bölge silinirken hata oluştu.");
                                //document.getElementById('personnelForm').reset();
                            }
                        })
                        .fail(function(data, xhr) {
                            //window.alert("Bölge silinirken hata oluştu 2.");
                        });
                }

            }
        })
        .fail(function(data, xhr) {
            $.post("https://afetkurtar.site/api/subpart/delete.php", JSON.stringify({
                    subpartID: subpartID
                }))
                .done(function(data, status, xhr) {
                    if (xhr.status == 201) {
                        //window.alert("Bölge silindi.");
                        //document.location.href = "https://afetkurtar.site/editDisaster.php?id=" + disasterID;
                        //document.location.reload();
                    } else {
                        //window.alert("Bölge silinirken hata oluştu. 0");
                        //document.getElementById('personnelForm').reset();
                    }
                })
                .fail(function(data, xhr) {
                    //window.alert("Bölge silinirken hata oluştu. 00");
                });

        });


}

function deleteDisaster(disasterID) {

    $.post("https://afetkurtar.site/api/subpart/search.php", JSON.stringify({
            disasterID: disasterID
        }))
        .done(function(data, status, xhr) {
            if (xhr.status == 200) {
                var subparts = data["records"];

                for (let i = 0; i < subparts.length; i++) {
                    deleteSubpartForDisaster(subparts[i]["subpartID"]);
                }

                $.post("https://afetkurtar.site/api/disasterEvents/delete.php", JSON.stringify({
                        disasterID: disasterID
                    }))
                    .done(function(data, status, xhr) {
                        if (xhr.status == 201) {
                            window.alert("Afet silindi.");
                            document.location.reload();
                        } else {
                            window.alert("Afet silinirken hata oluştu.");
                            //document.getElementById('personnelForm').reset();
                        }
                    })
                    .fail(function(data, xhr) {
                        //window.alert("Bölge silinirken hata oluştu. 00");
                    });


            } else {
                window.alert("Afet bölgeleri bulunurken hata oluştu.");
            }
        })
        .fail(function(data, xhr) {
            $.post("https://afetkurtar.site/api/disasterEvents/delete.php", JSON.stringify({
                    disasterID: disasterID
                }))
                .done(function(data, status, xhr) {
                    if (xhr.status == 201) {
                        window.alert("Afet silindi.");
                        document.location.reload();
                    } else {
                        window.alert("Afet silinirken hata oluştu.");
                        //document.getElementById('personnelForm').reset();
                    }
                })
                .fail(function(data, xhr) {
                    //window.alert("Bölge silinirken hata oluştu. 00");
                });
        });
}

function addPersonnelToSubpartForSmartAssignment(personnelID, subpartID) {

    $.post("https://afetkurtar.site/api/team/search.php", JSON.stringify({ assignedSubpartID: subpartID }))
        .done(function(data, status, xhr) {
            if (xhr.status == 200) {
                if (data.records.length != 0) {
                    var team = data.records[0];
                    var teamID = team.teamID;

                    $.post("https://afetkurtar.site/api/personnelUser/update.php", JSON.stringify({ personnelID: personnelID, teamID: teamID }))
                        .done(function(data, status, xhr) {
                            if (xhr.status == 201) {}
                        })
                        .fail(function(data, xhr) {});
                }
            }
        })
        .fail(function(data, xhr) {
            $.post("https://afetkurtar.site/api/team/create.php", JSON.stringify({ assignedSubpartID: subpartID }))
                .done(function(data, status, xhr) {
                    var teamID = data.id;

                    $.post("https://afetkurtar.site/api/personnelUser/update.php", JSON.stringify({ personnelID: personnelID, teamID: teamID }))
                        .done(function(data, status, xhr) {
                            if (xhr.status == 201) {}
                        })
                        .fail(function(data, xhr) {});
                })
                .fail(function(data, xhr) {});
        });

}

function doSmartAssignment() {
    var selectedSubparts = [];
    var toggles = document.getElementsByClassName("subpart-toggle");

    console.log(toggles);


    for (let i = 0; i < toggles.length; i++) {
        if (toggles[i].checked) {
            selectedSubparts.push(toggles[i].getAttribute("subpart-id"));
        }
    }

    var numberOfPeople = document.getElementById("smart-assignment-people").value.trim();

    if (selectedSubparts.length == 0) {
        window.alert("Lütfen bölge seçiniz");
        return false;
    }

    if (isNaN(numberOfPeople) || numberOfPeople == "") {
        alert("Atanacak kişi sayısı bilgisini kontrol ediniz.");
        return false;
    }

    $body = $("body");
    $body.addClass("loading");

    $.post("https://afetkurtar.site/api/smartAssignment.php", JSON.stringify({
            subpartIDs: selectedSubparts,
            numberOfPeople: numberOfPeople
        }))
        .done(function(data, status, xhr) {
            $body.removeClass("loading");

            var infoString = "";
            var assignments = data;


            for (var key in assignments) {
                infoString += document.getElementById("subpartAssignment" + key).getAttribute("subpart-name") + " bölgesi için önerilen kişi sayısı: " + assignments[key].length + "\n";
                // for (let i = 0; i < assignments[key].length; i++) {
                //     addPersonnelToSubpartForSmartAssignment(assignments[key][i]["personnelID"], assignments[key]);
                // }
            }
            infoString += "\nAtamayı gerçekleştirmek istiyor musunuz?";
            var confirmAlert = window.confirm(infoString);



            if (confirmAlert) {
                for (var key in assignments) {
                    for (let i = 0; i < assignments[key].length; i++) {
                        addPersonnelToSubpartForSmartAssignment(assignments[key][i]["personnelID"], assignments[key]);
                    }
                }
            }
            document.location.reload();

        })
        .fail(function(data, xhr) {
            $body.removeClass("loading");
            window.alert("Akıllı atama gerçekleştirilirken hata oluştu.");
        });

}
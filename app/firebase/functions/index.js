const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp();

exports.sendRequest = functions.https.onRequest(async (req, res) => {
    const token = req.query.token;
    console.log("Token Received: " + token);
    const message = req.query.text;
    console.log("Message Received: " + message);

    const notif = {
        data: {message},
        token: token
    };

    admin.messaging().send(notif)
        .then((response => {
            console.log("Sent Successful", response);
        })).catch((error) => {
        console.log("Error Sending Message", error);
    });
    res.response
});

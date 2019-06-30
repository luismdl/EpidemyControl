var express = require('express');
var app = express();
const bodyParser = require('body-parser');
var router = express.Router();
var ibmdb = require('ibm_db');


var connstring ="DATABASE=BLUDB;HOSTNAME=dashdb-txn-sbox-yp-lon02-01.services.eu-gb.bluemix.net;PORT=50000;PROTOCOL=TCPIP;UID=tfh44607;PWD=80n9br3ws89d@pqx;";

router.get('/usuario/:username', function(req, res, next) {
  var userN = req.params.username;

  ibmdb.open(connstring, function (err,conn) {
    if (err) return console.log(err);
    conn.query("SELECT * from TFMUSERS where username='"+userN +"'", function (err, data) {
      if (err) console.log(err);
      else{
        console.log(data[0])
        res.send(data[0]);
      }
      conn.close(function () {
        console.log('done');
      });
    });
  });
});

router.get('/usuario/:username/state', function(req, res, next) {
  var userN = req.params.username;

  ibmdb.open(connstring, function (err,conn) {
    if (err) return console.log(err);
    conn.query("SELECT STATE from TFMUSERS where username='"+userN +"'", function (err, data) {
      if (err) console.log(err);
      else{
        console.log(data[0])
        res.send(data[0]);
      }
      conn.close(function () {
        console.log('done');
      });
    });
  });
});

router.put('/usuario/:username/state', function(req, res, next) {
  var user = req.body;
  var userN = req.params.username;
  var query= "SELECT * from TFMUSERS where username='"+userN+"'"
  console.log(user)
  ibmdb.open(connstring, function (err,conn) {
    if (err) return console.log(err);
    conn.query(query, function (err, data) {
      if (err) console.log(err);
      else{
        if(data.length>0){
          var updt ="UPDATE TFMUSERS SET STATE ='"+user.STATE+"'where username='"+userN+"'"
          conn.query(updt, function (error, results, fields) {
            if (error) throw error;
            res.sendStatus(200);
          })
        }else{
          res.sendStatus(409);
        }
      } 
      conn.close(function () {
        console.log('done');
      });
    });
  });
});



router.post('/usuario', function(req, res, next) {
  var user = req.body;
  console.log(user)
  var query= "SELECT * from TFMUSERS where username='"+user.USERNAME +"' and password='"+user.PASSWORD+"'"
  ibmdb.open(connstring, function (err,conn) {
    if (err) return console.log(err);
    conn.query(query, function (err, data) {
      if (err) console.log(err);
      else{
        if(data.length>0){
          res.sendStatus(409);
        }else{
          var insrt ="INSERT INTO TFMUSERS (userName, email, password, birthDate, admin, state) VALUES ('"+ user.USERNAME+"', '"+user.EMAIL+"', '"+user.PASSOWRD+"', '"+user.BIRTHDATE+"', 0,0)"
          conn.query(insrt, function (error, results, fields) {
            if (error) throw error;
            res.sendStatus(201);
          })
        }
      } 
      conn.close(function () {
        console.log('done');
      });
    });
  });
});

router.put('/usuario/:username', function(req, res, next) {
  var user = req.body;
  var userN = req.params.username;
  var query= "SELECT * from TFMUSERS where username='"+userN+"'"
  console.log(user)
  ibmdb.open(connstring, function (err,conn) {
    if (err) return console.log(err);
    conn.query(query, function (err, data) {
      if (err) console.log(err);
      else{
        if(data.length>0){
          var updt ="UPDATE TFMUSERS SET password ='"+user.PASSWORD+"'where username='"+userN+"'"
          conn.query(updt, function (error, results, fields) {
            if (error) throw error;
            res.sendStatus(200);
          })
        }else{
          res.sendStatus(409);
        }
      } 
      conn.close(function () {
        console.log('done');
      });
    });
  });
});

router.delete('/usuario/:username', function(req, res, next) {
  var userN = req.params.username;
  var query= "SELECT * from TFMUSERS where username='"+userN+"'"
  ibmdb.open(connstring, function (err,conn) {
    if (err) return console.log(err);
    conn.query(query, function (err, data) {
      if (err) console.log(err);
      else{
        if(data.length>0){
          var del ="DELETE FROM TFMUSERS where username='"+userN+"'";
          conn.query(del, function (error, results, fields) {
            if (error) throw error;
            res.sendStatus(200);
          })
        }else{
          res.sendStatus(409);
        }
      } 
      conn.close(function () {
        console.log('done');
      });
    });
  });
});


app.use(bodyParser.json());
app.use(router);

app.listen(process.env.PORT || 3000);


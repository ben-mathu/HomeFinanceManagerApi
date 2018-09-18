var express = require('express'),
    app = express(),
    port = process.env.PORT || 3000,
    mongoose = require('mongoose'),
    Item = require('./api/models/financeManagerModel'),
    bodyParser = require('body-parser');

    //mongoose instantiated
    //and url connection
    mongoose.Promise = global.Promise;
    mongoose.connect('mongodb://localhost/financeManagerDb', { useNewUrlParser: true});

    app.use(bodyParser.urlencoded({ extended: true }));
    app.use(bodyParser.json());

    var routes = require('./api/routes/financeManagerRoutes'); // importing Routes
    routes(app);

    app.listen(port);

    console.log('Finance Manager API server started on: ' + port);

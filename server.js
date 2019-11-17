const express = require('express'),
    app = express(),
    port = process.env.PORT || 8000,
    path = require('path'),
    temphbs = require('express-handlebars'),
    mongoose = require('mongoose'),
    Item = require('./api/models/financeManagerModel'),
    User = require('./api/models/financierUserModel'),
    bodyParser = require('body-parser'),
    routes = require('./api/routes/index');


//mongoose instantiated
//and url connection
mongoose.Promise = global.Promise;
// mongoose.connect('mongodb://admin:JWMCEMHVPGQCUTOP@portal-ssl513-10.bmix-eu-gb-yp-8a0bb7ae-3707-4826-8a4c-c65f52d54179.1717821952.composedb.com:16447,portal-ssl505-38.bmix-eu-gb-yp-8a0bb7ae-3707-4826-8a4c-c65f52d54179.1717821952.composedb.com:16447/compose?authSource=admin&ssl=true', { useNewUrlParser: true});
mongoose.connect('mongodb://localhost:27017/financeManagerDb', { useNewUrlParser: true})

app.use(bodyParser.urlencoded({ extended: true }));
app.use(bodyParser.json());

app.use(express.static(path.join(__dirname, 'public')));
global.__basedir = __dirname;

// Set templating and view engine
app.set('views', path.join(__dirname, 'views'));
app.engine('handlebars', temphbs({defaultLayout: 'index'}));
app.set('view engine', 'handlebars');

routes(app);

app.listen(port);

console.log('Server started...');

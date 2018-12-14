var express = require('express');
var router = express.Router();
var itemList = require('../controllers/financeManagerController');

router.get('/', (req, res) => {
    res.render('home');
});

module.exports = router;
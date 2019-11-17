'use strict';
var express = require('express');
var router = express.Router();
var itemList = require('../controllers/financeManagerController');

// Item list Routes
router.route('/items')
  .get(itemList.list_all_items)
  .post(itemList.create_an_item)
  .delete(itemList.delete_all_items);

router.route('/items/:itemId')
  .get(itemList.read_an_item)
  .put(itemList.update_an_item)
  .delete(itemList.delete_an_item);

module.exports = router;

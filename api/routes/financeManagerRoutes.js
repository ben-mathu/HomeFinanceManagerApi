'use strict';
module.exports = function(app) {
  var itemList = require('../controllers/financeManagerController');

  // Item list Routes
  app.route('/items')
    .get(itemList.list_all_items)
    .post(itemList.create_an_item)
    .delete(itemList.delete_all_items);

  app.route('/items/:itemId')
    .get(itemList.read_an_item)
    .put(itemList.update_an_item)
    .delete(itemList.delete_an_item);
};

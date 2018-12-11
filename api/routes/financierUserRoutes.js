'use strict';
var express = require('express');
var router = express.Router();
var userList = require('../controllers/financierUserController');

router.route('/users')
  .get(userList.list_all_users)
  .post(userList.create_a_user)
  .delete(userList.delete_all_users);

router.route('/users/:userId')
  .get(userList.get_a_user)
  .put(userList.update_a_user)
  .delete(userList.delete_all_users);

module.exports = router;

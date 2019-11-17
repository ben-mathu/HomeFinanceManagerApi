'use strict';
var mongoose = require('mongoose');
var Schema = mongoose.Schema;

var UserSchema = new Schema({
  name: {
    type: String,
    require: 'Kindly enter your name'
  },
  email: {
    type: String,
    required: 'Kindly enter your email'
  },
  mobile: {
    type: String,
    required: 'Kindly enter your password'
  },
  password: {
    type: String,
    required: 'Kindly enter your email'
  },
  created_date: {
    type: String,
    default: Date.now
  }
});

module.exports = mongoose.model('User', UserSchema);

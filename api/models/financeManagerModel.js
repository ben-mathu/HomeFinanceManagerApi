'use strict';
var mongoose = require('mongoose');
var Schema = mongoose.Schema;

var ItemSchema = new Schema({
  name: {
    type: String,
    required: 'Kindly enter the name of the item'
  },
  created_date: {
    type: String,
    default: Date.now
  },
  status: {
    type: String,
    default: 'empty'
  }
});

module.exports = mongoose.model('Items', ItemSchema);

module.exports = function(app) {
  var items = require('./financeManagerRoutes');
  var users = require('./financierUserRoutes');
  app.use('/financier_api', items);
  app.use('/financier_api', users);
};

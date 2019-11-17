module.exports = function(app) {
  var items = require('./financeManagerRoutes');
  var users = require('./financierUserRoutes');
  var home = require('./commonRoutes');
  app.use('/financier_api', items);
  app.use('/financier_api', users);
  app.use('/', home);
};

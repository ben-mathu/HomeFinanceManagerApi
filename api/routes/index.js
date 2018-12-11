module.exports = function(app) {
  var items = require('./financeManagerRoutes');
  var users = require('./financierUserRoutes');
  app.use('/financier-api', items);
  app.use('/financier-api', users);
};

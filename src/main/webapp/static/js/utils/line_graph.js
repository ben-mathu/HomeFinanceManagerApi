class LineGraph {
    constructor(args) {
        this.property = args;
        this.canvas = args.canvas;
        this.transactions = args.transactions;
        this.context = this.canvas.getContext("2d");
        this.colors = args.colors;
        this.graphWidth = 12;
        this.xPadding = 30;
        this.yPadding = 15;
        this.labelPadding = 5;
        this.width = this.canvas.width;
        this.height = this.canvas.height;
        this.x = this.xPadding;
        this.y = this.height - this.yPadding;
        this.months = ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'];
        this.scale = 800;
        this.maxAmount = 8000;
        this.actualMaxAmount = 0;
        this.graphMaxAmount = 0;
        this.monthlyAmount = {};
        
        this.getFormatDate = function (date) {
            const dateTimeFormat = new Intl.DateTimeFormat('en', { year: 'numeric', month: '2-digit', day: '2-digit', hour: '2-digit', minute: '2-digit', hour12: false});
            const [{ value: month },,{ value: day },,{ value: year },,{value: hour},,{value: minute}] = dateTimeFormat .formatToParts(date );

            let dateStr = `${year}-${month}-${day} ${hour}:${minute}`;
            return dateStr;
        };
        
        this.getScale = function () {
            let value = this.maxAmount;
            if (value > 9999999 && value < 1000000000) {
                return 100000;
            } else if (value > 999999 && value < 100000000) {
                return 10000;
            } else if (value > 99999 && value < 10000000) {
                return 1000;
            } else if (value > 9999 && value < 100000) {
                return 100;
            } else if (value > 999 && value < 10000) {
                return 10;
            } else if (value > 99 && value < 1000) {
                return 1;
            } else if (value > 9 && value < 100) {
                return 1;
            } else if (value > 1) {
                return 1;
            }
        };
        
        this.getPlaceValue = function(value) {
            if (value > 9999999 && value < 1000000000) {
                return Math.ceil(value/ 1000000) * 1000000;
            } else if (value > 999999 && value < 100000000) {
                return Math.ceil(value/ 1000000) * 1000000;
            } else if (value > 99999 && value < 10000000) {
                return Math.ceil(value/ 100000) * 100000;
            } else if (value > 9999 && value < 100000) {
                return Math.ceil(value/ 10000) * 10000;
            } else if (value > 999 && value < 10000) {
                return Math.ceil(value/ 1000) * 1000;
            } else if (value > 99 && value < 1000) {
                return Math.ceil(value/ 100) * 100;
            } else if (value > 9 && value < 100) {
                return Math.ceil(value/ 10) * 10;
            } else if (value > 1) {
                return Math.ceil(value) * 1;
            }
        };
        
        this.getMaxAmount = function() {
            let keys = Object.keys(this.monthlyAmount);
            if (keys.length < 2) {
                return this.getPlaceValue(this.monthlyAmount[keys[0]]);
            } else {
                let temp = 0;
                let keys = Object.keys(this.monthlyAmount);
                keys.forEach(key => {
                    if (temp < this.monthlyAmount[key]) {
                        temp = this.monthlyAmount[key];
                    }
                });

                this.actualMaxAmount = temp;
                return this.getPlaceValue(temp);
            }
        };
        
        this.drawXAxis = function () {
            let ctx = this.context;
            ctx.save();
            ctx.beginPath();
            ctx.moveTo(this.x, this.y);
            ctx.lineTo(this.width - this.xPadding, this.y);
            ctx.strokeStyle = "#F8FFE1";
            ctx.lineWidth = 2;
            ctx.stroke();
        };
        
        this.addXLabels = function () {
            let ctx = this.context;
            ctx.fillStyle = "#F8FFE1";
            ctx.textAlign = "center";
            ctx.textBaseline = "middle";
            
            let length = this.width - this.xPadding;
            
            let labelx = this.x;
            let labely = this.y + 10;
            this.months.forEach(month => {
                ctx.save(); 
                ctx.translate(labelx, labely);
                ctx.fillText(month, 0, 0);
                ctx.restore();
                
                labelx += (this.width - this.xPadding) / 12;
                labely = this.y + 10;
            });
        };
        
        this.drawYAxis = function () {
            let ctx = this.context;
            ctx.save();
            ctx.beginPath();
            ctx.moveTo(this.x, this.y);
            ctx.lineTo(this.x, this.yPadding);
            ctx.strokeStyle = "#F8FFE1";
            ctx.lineWidth = 2;
            ctx.stroke();
        };
        
        this.addYLabels = function () {
            let scale = this.scale;
            let ctx = this.context;
            ctx.fillStyle = "#F8FFE1";
            ctx.textAlign = "center";
            ctx.textBaseline = "middle";
            
            let height = this.y - this.yPadding;
            
            let count = 0;
            let labelx = this.x - 15;
            let labely = this.y;
            let max = this.maxAmount / this.scale;
            
            if (max < 500) {
                max = 500;
                this.graphMaxAmount = 500 * this.scale;
            } else if (max > 500) {
                this.graphMaxAmount = max;
            }
            
            for (var i = 0; i <= max; i += 100) {
                ctx.save();
                ctx.translate(labelx, labely);
                ctx.fillText(i, 0, 0);
                ctx.restore();

                labelx = this.x - 15;
                labely -= height / max * 100;
            }
        };
        
        this.getFirstTransactionDate = function () {
            let timeStamp = "";
            this.transactions.forEach(transaction => {
                let paymentTime = transaction.payment_timestamp;
                
                let furthestTime = new Date(paymentTime);
                let time = new Date(timeStamp);
                
                if (furthestTime.getDate() > time.getDate()) {
                    timeStamp = transaction.payment_timestamp;
                }
            });
            
            return timeStamp;
        };

        this.sortedTransactionByDate = function () {
            for (var i = 0; i < this.transactions.length; i++) {
                if (i === this.transactions.length - 1) {
                    break;
                }
                
                let curr = this.transactions[i];
                let next = this.transactions[i + 1];
                
                let currDate = new Date(curr.payment_timestamp);
                let nextDate = new Date(next.payment_timestamp);
                if (currDate.getDate() > nextDate.getDate()) {
                    let temp = next;
                    this.transactions[i + 1] = this.transactions[i];
                    this.transactions[i] = temp;
                    i = 0;
                }
            }
            
            // get total amount
            this.transactions.forEach(transaction => {
                let date = new Date(transaction.payment_timestamp);
                let monIndex = date.getMonth() + 1;

                let prevAmount = this.monthlyAmount[monIndex] === undefined ? 0 : this.monthlyAmount[monIndex];
                this.monthlyAmount[monIndex] = transaction.amount + prevAmount;
            });
        };
        
        this.draw = function() {
            this.context.clearRect(0, 0, this.canvas.width, this.canvas.height);
            
            this.drawXAxis();
            this.addXLabels();
            this.drawYAxis();
            
            this.sortedTransactionByDate();
            this.maxAmount = this.getMaxAmount();
            this.scale = this.getScale();
            
            this.addYLabels();
            
            let totalAmount = 0;
            let colorIndex = 0;
            
            this.transactions.forEach(transaction => {
                totalAmount += transaction.amount;
            });

            let startAngle = 0;
            
            let keys = Object.keys(this.monthlyAmount);
            
            let startX;
            if (keys[0] === 0) {
                startX = this.x;
            } else if (keys[0] > 0) {
                startX = (this.width - this.xPadding) / 12 * keys[0];
            }
            
            let height = this.y - this.yPadding;
            
            let firstAmount = this.monthlyAmount[keys[0]]/this.scale;
            let actualScale = this.graphMaxAmount;
            let startY = this.y - (height * firstAmount / actualScale);
            
            let endX = (this.width - this.xPadding) / 12 * keys[1];
            
            let secondAmount = this.monthlyAmount[keys[1]];
            let endY = this.y - (height * secondAmount / actualScale);
            
            drawCircle(this.context,
                startX,
                startY,
                3,
                0,
                2 * Math.PI,
                this.colors[0]
            );
            
            for (var i = 2; i < keys.length; i++) {
                console.log(keys.length);
                if (i === keys.length) {
                    break;
                }

                drawLine(
                    this.context,
                    startX,
                    startY,
                    endX,
                    endY,
                    this.colors[0]
                );
                
                startX = endX;
                startY = endY;
                
                endX += (this.width - this.xPadding) / 12;
//                y = this.monthlyAmount[keys[i + 1]] / this.scale / 100;
                
                if (this.monthlyAmount[keys[i]] > this.monthlyAmount[keys[i + 1]]) {
                    endY = this.y - (height * this.monthlyAmount[keys[0]] / actualScale);
                } else if (this.monthlyAmount[keys[i]] === this.monthlyAmount[keys[i + 1]]) {
                    
                } else if (this.monthlyAmount[keys[i]] < this.monthlyAmount[keys[i + 1]]) {
                    endY = this.y - (height * this.monthlyAmount[keys[0]] / actualScale);
                }
                
                drawCircle(this.context,
                    startX,
                    startY,
                    3,
                    0,
                    2 * Math.PI,
                    this.colors[0]
                );
            }
            
//            add legends
            let legend = document.querySelector("legend[for='lineGraphCanvas']");
            let ul = document.createElement("ul");
            legend.innerHTML = "";
            legend.appendChild(ul);
            
            let li = document.createElement("li");
            
            li.style.listStyle = "none";
            li.style.padding = "5px";
            li.style.marginBottom = "5px";
            li.textContent = "Scale: 1:" + (this.scale === undefined ? " No transactions completed" : this.scale);
            ul.appendChild(li);
        };
    }
}

/**
 * 
 * @param {Context} ctx canvas context
 * @param {float} xFrom x coordinate from
 * @param {float} yFrom y coordinate from
 * @param {float} xTo x coordinate to
 * @param {float} yTo y coordinate to
 * @param {string} color stroke color
 */
function drawLine(ctx, xFrom, yFrom, xTo, yTo, color) {
    ctx.save();
    ctx.beginPath();
    ctx.moveTo(xFrom, yFrom);
    ctx.lineTo(xTo, yTo);
    ctx.strokeStyle = color;
    ctx.lineWidth = 1;
    ctx.stroke();
}

function drawCircle(ctx, centerX, centerY, radius, startAngle, endAngle, color) {
    ctx.fillStyle = color;
    ctx.beginPath();
    ctx.moveTo(centerX, centerY);
    ctx.arc(centerX, centerY, radius, startAngle, endAngle);
    ctx.closePath();
    ctx.fill();
}
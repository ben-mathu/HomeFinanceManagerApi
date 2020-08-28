class PieChart {
    constructor(arg) {
        this.property = arg;
        this.canvas = arg.canvas;
        this.jars = arg.jars;
        this.context = this.canvas.getContext("2d");
        this.colors = arg.colors;

        this.draw = function () {
            let totalAmount = 0;
            let colorIndex = 0;
            
            this.jars.forEach(jar => {
                totalAmount += jar.amount;
            });

            let startAngle = 0;
            this.jars.forEach(jar => {
                let amount = jar.amount;
                let sliceAngle = (amount/totalAmount) * 2 * Math.PI;

                drawSlice(
                    this.context,
                    this.canvas.width/2,
                    this.canvas.height/2,
                    Math.min(this.canvas.width/3, this.canvas.height/3),
                    startAngle,
                    startAngle + sliceAngle,
                    this.colors[colorIndex % this.colors.length]
                );

                startAngle += sliceAngle;
                colorIndex++;
            });

            // draw labels
            startAngle = 0;
            this.jars.forEach(jar => {
                let amount = jar.amount;
                let sliceAngle = (amount/totalAmount) * 2 * Math.PI;
                let pieRadius = Math.min(this.canvas.width/3, this.canvas.height/3);
                let labelX = this.canvas.width/2 + pieRadius/1.25 * Math.cos(startAngle + sliceAngle/2);
                let labelY = this.canvas.height/2 + pieRadius/1.25 * Math.sin(startAngle + sliceAngle/2);

                let labelText = Math.round(100 * amount/totalAmount) + "%";

                this.context.fillStyle = "white";
                this.context.font = "bold 12px Arial";
                this.context.fillText(labelText, labelX, labelY);
                startAngle += sliceAngle;
            });

            let legend = document.querySelector("legend[for='jarsCanvas']");
            let ul = document.createElement("ul");
            legend.innerHTML = "";
            legend.appendChild(ul);
            colorIndex = 0;
            this.jars.forEach(jar => {
                let li = document.createElement("li");

                li.style.listStyle = "none";
                li.style.borderLeft = "20px solid " + this.colors[colorIndex % this.colors.length];
                li.style.padding = "5px";
                li.textContent = jar.expense_type;
                ul.appendChild(li);
                colorIndex++;
            });
        };
    }
}

function drawSlice(ctx, centerX, centerY, radius, startAngle, endAngle, color) {
    ctx.fillStyle = color;
    ctx.beginPath();
    ctx.moveTo(centerX, centerY);
    ctx.arc(centerX, centerY, radius, startAngle, endAngle);
    ctx.closePath();
    ctx.fill();
}
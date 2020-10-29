var grid = `*, ,*,*,*,*,*, ,*, 
*, , ,*, ,*,*,*, , 
*,*, , , , , , ,*, 
*,*, , , ,*, ,*, , 
*, , , , , , , ,*,*
*,*, ,*, ,*, ,*, , 
*, , , ,*, , , , ,*
*,*, ,*, ,*, ,*, ,*
*, , ,*, , , , , ,*
*,*,*,*,*,*,*,*,*,*
`

var content;


for(let element of grid){
    if(element == ","){
        continue;
    }
    else if(element == " "){
        let newLabel = document.createElement("label");
        newLabel.className = "space";
        document.querySelector("content").appendChild(newLabel);
    }
    else if(element == "*"){
        let newLabel = document.createElement("label");
        newLabel.className = "block";
        document.querySelector("content").appendChild(newLabel);
    }
    else{
        document.querySelector("content").appendChild(document.createElement("br"));
    }
}
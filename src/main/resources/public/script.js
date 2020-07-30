let selected = -1;
let http = new XMLHttpRequest();
let locked = false;

const tokencx = 28.688 - 2.986;
const tokency = 30.36525 - 2.986;
const positionsmm = [14, 30, 46, 60, 74, 90, 106];
const positions = [52.913, 113.386, 173.858, 226.772, 279.686, 340.158, 400.631];
const xCords = [0, 3, 6, 6, 6, 3, 0, 0, 1, 3, 5, 5, 5, 3, 1, 1, 2, 3, 4, 4, 4, 3, 2, 2];
const yCords = [0, 0, 0, 3, 6, 6, 6, 3, 1, 1, 1, 3, 5, 5, 5, 3, 2, 2, 2, 3, 4, 4, 4, 3];

function sendRequestGET(path = '', query = '') {
    if (locked) return;
    locked = true;
    http.open('GET', path + '?' + query);
    http.send();
}

//update html after receiving http response
http.onreadystatechange = function () {
    if (this.readyState == 4 && this.status == 200) {
        locked = false;

        //0-23 are nodes, 24 is selection, 25 is turn, 26 is winner, 27 is type of action, 35 is autoplay(true/false)
        let response = this.response.split(',')
        document.getElementById('head').innerHTML = response[27] == 0 ? "Move" : ((response[27] == 1) ? "Insert" : "Take");   //update display
        document.getElementById('tokens_wh').innerHTML = response[28]   //number of white tokens
        document.getElementById('tokens_bl').innerHTML = response[29]   //number of black tokens
        document.getElementById('evaluation').innerHTML = response[30]  //evaluation by criteria
        document.getElementById('numOfMoves').innerHTML = response[31]  //simulated moves
        document.getElementById('duration').innerHTML = response[32]  //duration of best move calculation
        document.getElementById('hashmapEntries').innerHTML = response[33]  //number of hashmap entries
        document.getElementById('searchDepth').value = response[34]  //number of hashmap entries
        document.getElementById('monteCarlo').innerHTML = response[36]=='true' ? 'MonteCarlo' : 'Kriterien'  //number of hashmap entries
        document.getElementById('timeLimit').value = response[37] + "ms" //if time limit is reached bestMove() is interrupted

        updateTokens(response);
        updateSelection(response[24]);
        document.getElementById('turn').innerHTML = 'Turn: Player ' +
            ((response[25] == 1) ? 'White' : 'Black');
        if (response[26] != 0) document.getElementById('head').innerHTML = 'Game Over<br>' +
            ((response[25] == 1) ? 'Black' : 'White') + ' wins!';

        //if computers turn play best move
        if (response[26]==0 && response[35]==response[25]) sendRequestGET('bestMove')
    }
}

//update the tokens in the html whenever anything has changed
function updateTokens(response) {
    let res = "";
    for (let i = 0; i < 24; i++) {
        if (response[i] == 0) continue;
        let l = positions[xCords[i]] - tokencx;
        let t = positions[yCords[i]] - tokency;
        let request = `onclick="sendRequestGET('click','node=` + i + `')"`
        res += '<img class="token" ' + request + ' src="images/' + ((response[i] == 1) ? 'white' : 'black') +
            '.svg" style="position:absolute;left:' + l + 'px;top:' + t + 'px;">';
    }
    document.getElementById('tokens').innerHTML = res;
}

//set depth to input value
function setDepth() {
    let depth = parseInt(document.getElementById('searchDepth').value)
    if (typeof depth == 'number') sendRequestGET('setDepth','depth=' + depth)
}
//set depth to input value
function setTimeLimit() {
    let timeLimit = parseInt(document.getElementById('timeLimit').value)
    if (typeof timeLimit == 'number') sendRequestGET('timeLimit','limit=' + timeLimit)
}

//setting up the the html when the page is loaded
function setup() {
    sendRequestGET('click', 'node=-1');
    let res = ""
    for (let i = 0; i < 24; i++) {
        let l = positionsmm[xCords[i]];
        let t = positionsmm[yCords[i]];
        let request = `onclick="sendRequestGET('click','node=` + i + `')"`
        res += '<circle r="8" ' + request + ' cy="' + (t + 181) + '" cx="' + (l - 4) +
            '" class="selector" id="node' + i + '" style="opacity:0;fill:#000000;' +
            'fill-opacity:1;position:absolute;stroke:none;stroke-width:0.60463309" />'
    }
    document.getElementById('selection').innerHTML = res;
}

function updateSelection(node) {
    if (selected != -1) document.getElementById('node' + selected).style.opacity = '0'
    if (node != -1) document.getElementById('node' + node).style.opacity = '0.5'
    selected = node;
}
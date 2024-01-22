window.onload = function () {
  console.log("Test Log")


  let btn = document.getElementById("b1")
  let coinname = document.getElementById("coinname")
  let currentprice = document.getElementById("currentprice")
  let datasymbol = document.getElementById("datasymbol")
  let img = document.getElementById("coinimg")
  

  btn.addEventListener("click", function () {
    let value = document.getElementById("fname").value
    fetch(`https://api.coingecko.com/api/v3/coins/${value}`)
      .then((res) => res.json())
      .then((data) => {
        if (data.error) {
          document.getElementById("error").style.display = "block"
          coinname.innerHTML = "-"
          currentprice.innerHTML = "-"
          datasymbol.innerHTML ="-"
          img.src = "none"
        } else {
          document.getElementById("error").style.display = "none"
          console.log(data)
          coinname.innerHTML = data.name
          currentprice.innerHTML = "$" + data.market_data.current_price.usd
          datasymbol.innerHTML = data.symbol
          img.src = data.image.large
        }

      })


  })

}
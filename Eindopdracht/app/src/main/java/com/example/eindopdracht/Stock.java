package com.example.eindopdracht;

public class Stock {
        private  String StockName="";
        private  String Price="";
        private  String daychange="";

        /*********** Set Methods ******************/

        public void setStockName(String StockName)
        {
            this.StockName = StockName;
        }

        public void setPrice(String Price)
        {
            this.Price = Price;
        }

        public void setDaychange(String change)
        {
            this.daychange = change;
        }

        /*********** Get Methods ****************/

        public String getStockName()
        {
            return this.StockName;
        }

        public String getPrice()
        {
            return this.Price;
        }

        public String getDaychange()
        {
            return this.daychange;
        }
}

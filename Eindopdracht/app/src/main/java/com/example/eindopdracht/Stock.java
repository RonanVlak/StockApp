package com.example.eindopdracht;
import java.io.Serializable;
public class Stock implements Serializable {
    String StockName, Price, daychange, PrevClose, Currency, ExchangeName, InstrumentType;
        public Stock() {
            StockName = new String ("");
            Price = new String ("");
            daychange = new String ("");
            PrevClose = new String ("");
            Currency = new String ("");
            ExchangeName = new String ("");
            InstrumentType = new String ("");
        }
        /*********** Set Methods ******************/

        public void setStockName(String stockName)
        {
            this.StockName = stockName;
        }
        public void setCurrency(String currency)
        {
            this.Currency = currency;
        }
        public void setExchangeName(String exchangename)
        {
            this.ExchangeName = exchangename;
        }
        public void setInstrumentType(String intstrumenttype)
        {
            this.InstrumentType = intstrumenttype;
        }

        public void setPrice(String price)
        {
            this.Price = price;
        }

        public void setDaychange(String change)
        {
            this.daychange = change;
        }

        public void setPrevClose(String prevclose)
        {
            this.PrevClose = prevclose;
        }


        /*********** Get Methods ****************/

        public String getStockName()
        {
            return this.StockName;
        }
        public String getInstrumentType()
        {
            return this.InstrumentType;
        }
        public String getExchangeName()
        {
            return this.ExchangeName;
        }
        public String getCurrency ()
        {
            return this.Currency;
        }
        public String getPrice()
        {
            return this.Price;
        }

        public String getDaychange()
        {
            return this.daychange;
        }
        public String getPrevClose() {
            return this.PrevClose;
        }

}

package com.jk.labs.fx.qual_engine.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Entity
@Table(name = "fx_trade")
@Data
@EqualsAndHashCode(callSuper = true)
public class FxTrade extends BaseEntity {

    @Column(name = "customer_id")
    private String customerId;

    @Column(name = "from_currency")
    private String fromCurrency;

    @Column(name = "to_currency")
    private String toCurrency;

    @Column(name = "from_currency_qyty")
    private Integer fromCurrencyQty;

    @Column(name = "to_currency_qyty")
    private Integer toCurrencyQty;

    @Column(name = "trading_books_csv", length = 500)
    private String tradingBooksCsv;

    @Column(name = "to_currency_rate")
    private BigDecimal toCurrencyRate;

    @Column(name = "trade_total")
    private BigDecimal tradeTotal;

    @Column(name = "trade_discount")
    private BigDecimal tradeDiscount;

    @Column(name = "trade_price")
    private BigDecimal tradePriace;
}

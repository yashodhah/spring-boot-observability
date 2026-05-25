package com.teamates.messaging;

import com.teamates.model.Order;

import java.time.Instant;

// FLAW: event class mixes event metadata with the full Order entity
// FLAW: not immutable – setters allow mutation after creation
// FLAW: should use a record or have final fields
public class OrderEvent {

    private String eventType;   // FLAW: should be an enum, not String
    private Order order;        // FLAW: embeds full entity – should be a lightweight DTO
    private Instant eventTime;
    private String source;      // FLAW: always "teamates-service" but not enforced

    public OrderEvent() {}

    public OrderEvent(String eventType, Order order) {
        this.eventType = eventType;
        this.order = order;
        this.eventTime = Instant.now();
        this.source = "teamates-service";
    }

    // FLAW: public setters allow mutation
    public void setEventType(String eventType) { this.eventType = eventType; }
    public void setOrder(Order order) { this.order = order; }
    public void setEventTime(Instant eventTime) { this.eventTime = eventTime; }
    public void setSource(String source) { this.source = source; }

    public String getEventType() { return eventType; }
    public Order getOrder() { return order; }
    public Instant getEventTime() { return eventTime; }
    public String getSource() { return source; }
}

package net.huansi.equipment.equipmentapp.entity;

public class TokenInfo {

    /**
     * access_token : eyJraWQiOiIxTkJ3QTJDeWpKRmdDYU5SOXlXZW1jY2ZaaDFjZ19ET1haWXVWcS1oX2RFIiwiYWxnIjoiUlMyNTYifQ.eyJ2ZXIiOjEsImp0aSI6IkFULlJuTlhBN0dBTm5HYTNNdWJnTG1pQVhuTXg2ODlwTTZrU3hHeXo0RWkxMHciLCJpc3MiOiJodHRwczovL25pa2UtcWEub2t0YXByZXZpZXcuY29tL29hdXRoMi9hdXNhMG1jb3JucFpMaTBDNDBoNyIsImF1ZCI6Imh0dHBzOi8vbmlrZS1xYS5va3RhcHJldmlldy5jb20iLCJpYXQiOjE1NTE5MjkxNDQsImV4cCI6MTU1MTkzMjc0NCwiY2lkIjoidGVzdGZhY3RvcnkuZ3NtLmJvbSIsInNjcCI6WyJkZWZhdWx0Il0sInN1YiI6InRlc3RmYWN0b3J5LmdzbS5ib20ifQ.IAbW38K5D_yfstu45dgyFKmMhxRTlGM0oFfIagJk8GjMnPe5Qvk7GwQpjUDKeUXH5OQI5rc4DvPlv3AzD7Z11afvFMT3lQl9YTNfQBhD46Nfi5v04LnRKuznmcrKm6f0sQfg_pmrr4-owwIkiDNTXTqFSmoF9E0fzn4YH1c_S8Izvqh5LgJtK54I1H4JTSKln0-3ad-Im_DNeLXa_Mm6jmIvfDWHKDMi5-qHXfO09jOv0jid0Vt-0qaZuNfMqClAuVPlqlx-huHauFbYuhJj5U0FzaqWVTc1E2oAa6N66k9T9aqPz4eeaqjMUTFB8Of05NNP7oij_vsDAZqpsa6IVQ
     * token_type : Bearer
     * expires_in : 3600
     * scope : default
     */

    private String access_token;
    private String token_type;
    private int expires_in;
    private String scope;

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getToken_type() {
        return token_type;
    }

    public void setToken_type(String token_type) {
        this.token_type = token_type;
    }

    public int getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(int expires_in) {
        this.expires_in = expires_in;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }
}

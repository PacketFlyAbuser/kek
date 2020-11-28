// 
// Decompiled by Procyon v0.5.36
// 

package nonapi.io.github.classgraph.json;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.List;

class JSONObject
{
    /* synthetic */ CharSequence objectId;
    /* synthetic */ List<Map.Entry<String, Object>> items;
    
    void toJSONString(final Map<ReferenceEqualityKey<JSONReference>, CharSequence> map, final boolean b, final int n, final int n2, final StringBuilder sb) {
        final boolean b2 = n2 > 0;
        final int size = this.items.size();
        int n3;
        if (b) {
            n3 = size;
        }
        else {
            n3 = 0;
            final Iterator<Map.Entry<String, Object>> iterator = this.items.iterator();
            while (iterator.hasNext()) {
                if (iterator.next().getValue() != null) {
                    ++n3;
                }
            }
        }
        if (this.objectId == null && n3 == 0) {
            sb.append("{}");
        }
        else {
            sb.append(b2 ? "{\n" : "{");
            if (this.objectId != null) {
                if (b2) {
                    JSONUtils.indent(n + 1, n2, sb);
                }
                sb.append('\"');
                sb.append("__ID");
                sb.append(b2 ? "\": " : "\":");
                JSONSerializer.jsonValToJSONString(this.objectId, map, b, n + 1, n2, sb);
                if (n3 > 0) {
                    sb.append(',');
                }
                if (b2) {
                    sb.append('\n');
                }
            }
            int i = 0;
            int n4 = 0;
            while (i < size) {
                final Map.Entry<String, Object> entry = this.items.get(i);
                final Object value = entry.getValue();
                if (value != null || b) {
                    final String s = entry.getKey();
                    if (s == null) {
                        throw new IllegalArgumentException("Cannot serialize JSON object with null key");
                    }
                    if (b2) {
                        JSONUtils.indent(n + 1, n2, sb);
                    }
                    sb.append('\"');
                    JSONUtils.escapeJSONString(s, sb);
                    sb.append(b2 ? "\": " : "\":");
                    JSONSerializer.jsonValToJSONString(value, map, b, n + 1, n2, sb);
                    if (++n4 < n3) {
                        sb.append(',');
                    }
                    if (b2) {
                        sb.append('\n');
                    }
                }
                ++i;
            }
            if (b2) {
                JSONUtils.indent(n, n2, sb);
            }
            sb.append('}');
        }
    }
    
    public JSONObject(final int initialCapacity) {
        this.items = new ArrayList<Map.Entry<String, Object>>(initialCapacity);
    }
    
    public JSONObject(final List<Map.Entry<String, Object>> items) {
        this.items = items;
    }
}

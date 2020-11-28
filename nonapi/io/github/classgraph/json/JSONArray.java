// 
// Decompiled by Procyon v0.5.36
// 

package nonapi.io.github.classgraph.json;

import java.util.ArrayList;
import java.util.Map;
import java.util.List;

class JSONArray
{
    /* synthetic */ List<Object> items;
    
    void toJSONString(final Map<ReferenceEqualityKey<JSONReference>, CharSequence> map, final boolean b, final int n, final int n2, final StringBuilder sb) {
        final boolean b2 = n2 > 0;
        final int size = this.items.size();
        if (size == 0) {
            sb.append("[]");
        }
        else {
            sb.append('[');
            if (b2) {
                sb.append('\n');
            }
            for (int i = 0; i < size; ++i) {
                final Object value = this.items.get(i);
                if (b2) {
                    JSONUtils.indent(n + 1, n2, sb);
                }
                JSONSerializer.jsonValToJSONString(value, map, b, n + 1, n2, sb);
                if (i < size - 1) {
                    sb.append(',');
                }
                if (b2) {
                    sb.append('\n');
                }
            }
            if (b2) {
                JSONUtils.indent(n, n2, sb);
            }
            sb.append(']');
        }
    }
    
    public JSONArray() {
        this.items = new ArrayList<Object>();
    }
    
    public JSONArray(final List<Object> items) {
        this.items = items;
    }
}

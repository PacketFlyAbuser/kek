// 
// Decompiled by Procyon v0.5.36
// 

package org.spongepowered.asm.util.perf;

import java.util.Arrays;
import com.google.common.base.Joiner;
import java.text.DecimalFormat;
import org.spongepowered.asm.util.PrettyPrinter;
import java.util.Collections;
import java.util.Collection;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Map;
import java.util.List;
import java.util.Deque;

public final class Profiler
{
    private /* synthetic */ boolean active;
    private final /* synthetic */ Deque<Section> stack;
    private final /* synthetic */ List<String> phases;
    private final /* synthetic */ Map<String, Section> sections;
    
    public Section begin(final String... array) {
        return this.begin(0, array);
    }
    
    void end(final Section obj) {
        try {
            Section section;
            final Section obj2 = section = this.stack.pop();
            while (section != obj) {
                if (section == null && this.active) {
                    if (obj2 == null) {
                        throw new IllegalStateException("Attempted to pop " + obj + " but the stack is empty");
                    }
                    throw new IllegalStateException("Attempted to pop " + obj + " which was not in the stack, head was " + obj2);
                }
                else {
                    section = this.stack.pop();
                }
            }
        }
        catch (NoSuchElementException ex) {
            if (this.active) {
                throw new IllegalStateException("Attempted to pop " + obj + " but the stack is empty");
            }
        }
    }
    
    public void reset() {
        final Iterator<Section> iterator = this.sections.values().iterator();
        while (iterator.hasNext()) {
            iterator.next().invalidate();
        }
        this.sections.clear();
        this.phases.clear();
        this.phases.add("Initial");
        this.stack.clear();
    }
    
    public void mark(final String s) {
        long n = 0L;
        final Iterator<Section> iterator = this.sections.values().iterator();
        while (iterator.hasNext()) {
            n += iterator.next().getTime();
        }
        if (n == 0L) {
            this.phases.set(this.phases.size() - 1, s);
            return;
        }
        this.phases.add(s);
        final Iterator<Section> iterator2 = this.sections.values().iterator();
        while (iterator2.hasNext()) {
            iterator2.next().mark();
        }
    }
    
    public Profiler() {
        this.sections = new TreeMap<String, Section>();
        this.phases = new ArrayList<String>();
        this.stack = new LinkedList<Section>();
        this.phases.add("Initial");
    }
    
    public Section get(final String s) {
        Section section = this.sections.get(s);
        if (section == null) {
            section = (this.active ? new LiveSection(s, this.phases.size() - 1) : new Section(s));
            this.sections.put(s, section);
        }
        return section;
    }
    
    public Collection<Section> getSections() {
        return Collections.unmodifiableCollection((Collection<? extends Section>)this.sections.values());
    }
    
    public Section begin(final int n, String string) {
        boolean root = (n & 0x1) != 0x0;
        final boolean fine = (n & 0x2) != 0x0;
        String string2 = string;
        final Section section = this.stack.peek();
        if (section != null) {
            string2 = section.getName() + (root ? " -> " : ".") + string2;
            if (section.isRoot() && !root) {
                final int lastIndex = section.getName().lastIndexOf(" -> ");
                string = ((lastIndex > -1) ? section.getName().substring(lastIndex + 4) : section.getName()) + "." + string;
                root = true;
            }
        }
        Section section2 = this.get(root ? string : string2);
        if (root && section != null && this.active) {
            section2 = this.getSubSection(string2, section.getName(), section2);
        }
        section2.setFine(fine).setRoot(root);
        this.stack.push(section2);
        return section2.start();
    }
    
    boolean isHead(final Section section) {
        return this.stack.peek() == section;
    }
    
    private void printSectionRow(final PrettyPrinter prettyPrinter, final int n, final int[] array, final Section section, final boolean b) {
        final boolean b2 = section.getDelegate() != section;
        final Object[] array2 = new Object[n];
        int n2 = 1;
        if (b) {
            array2[0] = (b2 ? ("  > " + section.getBaseName()) : section.getName());
        }
        else {
            array2[0] = (b2 ? "+ " : "  ") + section.getName();
        }
        for (final long lng : section.getTimes()) {
            if (n2 == array[1]) {
                array2[n2++] = section.getTotalTime() + " ms";
            }
            if (n2 >= array[2] && n2 < array2.length) {
                array2[n2++] = lng + " ms";
            }
        }
        array2[array[3]] = section.getTotalCount();
        array2[array[4]] = new DecimalFormat("   ###0.000 ms").format(section.getTotalAverageTime());
        for (int j = 0; j < array2.length; ++j) {
            if (array2[j] == null) {
                array2[j] = "-";
            }
        }
        prettyPrinter.tr(array2);
    }
    
    private Section getSubSection(final String s, final String s2, final Section section) {
        Section section2 = this.sections.get(s);
        if (section2 == null) {
            section2 = new SubSection(s, this.phases.size() - 1, s2, section);
            this.sections.put(s, section2);
        }
        return section2;
    }
    
    public Section begin(final String s) {
        return this.begin(0, s);
    }
    
    public PrettyPrinter printer(final boolean b, final boolean b2) {
        final PrettyPrinter prettyPrinter = new PrettyPrinter();
        final int n = this.phases.size() + 4;
        final int[] array = { 0, 1, 2, n - 2, n - 1 };
        final Object[] array2 = new Object[n * 2];
        int i = 0;
        int n2 = 0;
        while (i < n) {
            array2[n2 + 1] = PrettyPrinter.Alignment.RIGHT;
            if (i == array[0]) {
                array2[n2] = (b2 ? "" : "  ") + "Section";
                array2[n2 + 1] = PrettyPrinter.Alignment.LEFT;
            }
            else if (i == array[1]) {
                array2[n2] = "    TOTAL";
            }
            else if (i == array[3]) {
                array2[n2] = "    Count";
            }
            else if (i == array[4]) {
                array2[n2] = "Avg. ";
            }
            else if (i - array[2] < this.phases.size()) {
                array2[n2] = this.phases.get(i - array[2]);
            }
            else {
                array2[n2] = "";
            }
            n2 = ++i * 2;
        }
        prettyPrinter.table(array2).th().hr().add();
        for (final Section section : this.sections.values()) {
            if (!section.isFine() || b) {
                if (b2 && section.getDelegate() != section) {
                    continue;
                }
                this.printSectionRow(prettyPrinter, n, array, section, b2);
                if (!b2) {
                    continue;
                }
                for (final Section section2 : this.sections.values()) {
                    final Section delegate = section2.getDelegate();
                    if ((!section2.isFine() || b) && delegate == section) {
                        if (delegate == section2) {
                            continue;
                        }
                        this.printSectionRow(prettyPrinter, n, array, section2, b2);
                    }
                }
            }
        }
        return prettyPrinter.add();
    }
    
    public void setActive(final boolean active) {
        if ((!this.active && active) || !active) {
            this.reset();
        }
        this.active = active;
    }
    
    static {
        ROOT = 1;
        FINE = 2;
    }
    
    public Section begin(final int n, final String... array) {
        return this.begin(n, Joiner.on('.').join((Object[])array));
    }
    
    public class Section
    {
        private /* synthetic */ String info;
        private final /* synthetic */ String name;
        private /* synthetic */ boolean root;
        protected /* synthetic */ boolean invalidated;
        private /* synthetic */ boolean fine;
        
        public void setInfo(final String info) {
            this.info = info;
        }
        
        public double getTotalSeconds() {
            return 0.0;
        }
        
        Section getDelegate() {
            return this;
        }
        
        void mark() {
        }
        
        Section setRoot(final boolean root) {
            this.root = root;
            return this;
        }
        
        public Section end() {
            if (!this.invalidated) {
                Profiler.this.end(this);
            }
            return this;
        }
        
        public long[] getTimes() {
            return new long[1];
        }
        
        public double getAverageTime() {
            return 0.0;
        }
        
        public String getBaseName() {
            return this.name;
        }
        
        public int getCount() {
            return 0;
        }
        
        public long getTime() {
            return 0L;
        }
        
        public String getName() {
            return this.name;
        }
        
        protected Section stop() {
            return this;
        }
        
        public String getInfo() {
            return this.info;
        }
        
        public long getTotalTime() {
            return 0L;
        }
        
        Section(final String s) {
            this.name = s;
            this.info = s;
        }
        
        @Override
        public final String toString() {
            return this.name;
        }
        
        Section start() {
            return this;
        }
        
        public int getTotalCount() {
            return 0;
        }
        
        public boolean isRoot() {
            return this.root;
        }
        
        public boolean isFine() {
            return this.fine;
        }
        
        public double getSeconds() {
            return 0.0;
        }
        
        public double getTotalAverageTime() {
            return 0.0;
        }
        
        static {
            SEPARATOR_CHILD = ".";
            SEPARATOR_ROOT = " -> ";
        }
        
        Section invalidate() {
            this.invalidated = true;
            return this;
        }
        
        Section setFine(final boolean fine) {
            this.fine = fine;
            return this;
        }
        
        public Section next(final String s) {
            this.end();
            return Profiler.this.begin(s);
        }
    }
    
    class LiveSection extends Section
    {
        private /* synthetic */ int count;
        private /* synthetic */ long time;
        private /* synthetic */ long start;
        private /* synthetic */ long[] times;
        private /* synthetic */ long markedTime;
        private /* synthetic */ int cursor;
        private /* synthetic */ int markedCount;
        
        LiveSection(final String s, final int cursor) {
            super(s);
            this.cursor = 0;
            this.times = new long[0];
            this.start = 0L;
            this.cursor = cursor;
        }
        
        @Override
        public long getTotalTime() {
            return this.time + this.markedTime;
        }
        
        @Override
        void mark() {
            if (this.cursor >= this.times.length) {
                this.times = Arrays.copyOf(this.times, this.cursor + 4);
            }
            this.times[this.cursor] = this.time;
            this.markedTime += this.time;
            this.markedCount += this.count;
            this.time = 0L;
            this.count = 0;
            ++this.cursor;
        }
        
        @Override
        public double getTotalAverageTime() {
            return (this.count > 0) ? ((this.time + this.markedTime) / (double)(this.count + this.markedCount)) : 0.0;
        }
        
        @Override
        public int getTotalCount() {
            return this.count + this.markedCount;
        }
        
        @Override
        public double getAverageTime() {
            return (this.count > 0) ? (this.time / (double)this.count) : 0.0;
        }
        
        @Override
        public double getSeconds() {
            return this.time * 0.001;
        }
        
        @Override
        public long[] getTimes() {
            final long[] array = new long[this.cursor + 1];
            System.arraycopy(this.times, 0, array, 0, Math.min(this.times.length, this.cursor));
            array[this.cursor] = this.time;
            return array;
        }
        
        @Override
        public long getTime() {
            return this.time;
        }
        
        @Override
        Section start() {
            this.start = System.currentTimeMillis();
            return this;
        }
        
        @Override
        protected Section stop() {
            if (this.start > 0L) {
                this.time += System.currentTimeMillis() - this.start;
            }
            this.start = 0L;
            ++this.count;
            return this;
        }
        
        @Override
        public double getTotalSeconds() {
            return (this.time + this.markedTime) * 0.001;
        }
        
        @Override
        public int getCount() {
            return this.count;
        }
        
        @Override
        public Section end() {
            this.stop();
            if (!this.invalidated) {
                Profiler.this.end(this);
            }
            return this;
        }
    }
    
    class SubSection extends LiveSection
    {
        private final /* synthetic */ Section root;
        private final /* synthetic */ String baseName;
        
        @Override
        public Section end() {
            this.root.stop();
            return super.end();
        }
        
        @Override
        Section invalidate() {
            this.root.invalidate();
            return super.invalidate();
        }
        
        @Override
        public Section next(final String s) {
            super.stop();
            return this.root.next(s);
        }
        
        @Override
        Section start() {
            this.root.start();
            return super.start();
        }
        
        @Override
        public String getBaseName() {
            return this.baseName;
        }
        
        @Override
        Section getDelegate() {
            return this.root;
        }
        
        SubSection(final String s, final int n, final String baseName, final Section root) {
            super(s, n);
            this.baseName = baseName;
            this.root = root;
        }
        
        @Override
        public void setInfo(final String s) {
            this.root.setInfo(s);
            super.setInfo(s);
        }
    }
}

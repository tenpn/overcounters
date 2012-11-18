(ns overcounters.core)

(use 'overtone.live)

(defn n 
  ([note-name duration] {:pitch (note->hz note-name) :duration duration})
  ([note-name] (n note-name 1)))

(def closenotes [(n :G4) (n :A4) (n :F4) (n :F3) (n :C4 2)])

(defn note->hz [music-note] (midi->hz(note music-note))) 

(definst saw-wave-inst [freq 440 attack 0.01 sustain 0.4 release 0.1 vol 0.4] 
  (* (env-gen (lin-env attack sustain release) 1 1 0 1 FREE)
     (saw freq)
     vol))

(defn saw-wave [note-name] (saw-wave-inst (note->hz note-name)))

(def one-twenty-bpm (metronome 120))

; indexed sequence. [a b c] becomes [[0 a] [1 b] [2 c]]
(defn iseq [seq] 
     (map-indexed (fn [i d] [i d]) seq))

; (play nome note-seq)
;  plays each note of note-seq on each beat of nome, 
;  waits a beat,
;  then repeats.
(defn play [nome notes]    
  (let [beat (nome)]
       (doseq [[i {duration :duration pitch :pitch}] (iseq notes)]
              (at (nome (+ i beat)) (saw-wave-inst pitch)))
       (apply-at (nome (+ (count notes) beat 1))
                 play nome notes [])))

(stop)

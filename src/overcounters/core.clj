(ns overcounters.core)

(use 'overtone.live)

(def closenotes [:G4 :A4 :F4 :F3 :C4])

(defn note->hz [music-note] (midi->hz(note music-note))) 

(definst saw-wave-inst [freq 440 attack 0.01 sustain 0.4 release 0.1 vol 0.4] 
  (* (env-gen (lin-env attack sustain release) 1 1 0 1 FREE)
     (saw freq)
     vol))

(defn saw-wave [note-name] (saw-wave-inst (note->hz note-name)))

(def one-twenty-bpm (metronome 120))

(defn iseq [seq] 
     (map-indexed (fn [i d] [i d]) seq))

; (play nome note-seq)
;  plays each note of note-seq on each beat of nome, 
;  waits a beat,
;  then repeats.
(defn play [nome notes]    
  (let [beat (nome)]
       (doseq [[i n] (iseq notes)]
              (at (nome (+ i beat)) (saw-wave n)))
       (apply-at (nome (+ (count notes) beat 1))
                 play nome notes [])))

(stop)
